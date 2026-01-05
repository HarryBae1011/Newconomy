package com.newconomy.news.service;

import com.newconomy.global.error.exception.handler.GeneralHandler;
import com.newconomy.global.response.status.ErrorStatus;
import com.newconomy.news.crawling.NewsCrawlingService;
import com.newconomy.news.domain.News;
import com.newconomy.news.domain.NewsTerm;
import com.newconomy.news.dto.NewsResponseDTO;
import com.newconomy.news.enums.NewsCategory;
import com.newconomy.news.properties.NaverNewsProperties;
import com.newconomy.news.repository.NewsRepository;
import com.newconomy.news.repository.NewsTermRepository;
import com.newconomy.term.dto.TermResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NewsService {

    private final NaverNewsProperties naverNewsProperties;
    private final NewsRepository newsRepository;
    private final NewsCrawlingService newsCrawlingService;
    private final NewsTermRepository newsTermRepository;

    public List<NewsResponseDTO.SingleNewsDTO> viewNews(NewsCategory newsCategory, Pageable limit) {
        return newsRepository.searchNews(limit, newsCategory);
    }

    public NewsResponseDTO.SingleNewsViewDTO viewSingleNews(Long newsId) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.NEWS_NOT_FOUND));

        return NewsResponseDTO.SingleNewsViewDTO.builder()
                .newsId(news.getId())
                .title(news.getTitle())
                .fullContent(news.getFullContent())
                .newsImgUrl(news.getNewsImgUrl())
                .newsCategory(news.getNewsCategory())
                .source(news.getSource())
                .publishedAt(news.getPublishedAt())
                .build();
    }

    @Transactional
    public List<NewsResponseDTO.SingleNewsDTO> searchHeadLineNews(String newsCategory) {
        // 기사 헤드라인을 가져올 url
        String newsUrl;

        // newsCategory가 없으면 '경제' 헤드라인
        if (newsCategory == null || newsCategory.isBlank()) {
            newsUrl = naverNewsProperties.getEconomy().get("main");

        } else {
            // newsCategory가 있으면 해당 섹션 헤드라인
            newsUrl = naverNewsProperties.getEconomy().get(NewsCategory.toNewsCategory(newsCategory).toString().toLowerCase());

            if (newsUrl == null) {
                throw new GeneralHandler(ErrorStatus.NEWS_CATEGORY_NOT_FOUND);
            }

        }

        return scrapeNaverNews(newsUrl, newsCategory);
    }

    private List<NewsResponseDTO.SingleNewsDTO> scrapeNaverNews(String url, String newsCategory) {
        // 뉴스 카테고리 결정
        NewsCategory category = newsCategory == null || newsCategory.isBlank()
                ? NewsCategory.MAIN
                : NewsCategory.toNewsCategory(newsCategory);

        List<News> newsList = new ArrayList<>();

        try {
            // Url에서 스크랩할 문서 가져오기
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(10000)
                    .get();

            // 네이버 뉴스 헤드라인 선택자
            Elements newsElements = doc.select("li.sa_item");

            // 가져올 기사 개수 (기본 10개)
            int limit = Math.min(10, newsElements.size());

            for (int i = 0; i < limit; i++) {
                Element newsElement = newsElements.get(i);

                // 제목과 URL
                Element titleLink = newsElement.selectFirst("a.sa_text_title");
                // 이미지
                Element img = newsElement.selectFirst("img._LAZY_LOADING");
                // 요약
                Element lede = newsElement.selectFirst("div.sa_text_lede");
                // 언론사
                Element press = newsElement.selectFirst("div.sa_text_press");

                if (titleLink != null) {
                    String title = titleLink.text();
                    String naverUrl = titleLink.attr("abs:href");
                    String newsImgUrl = img != null ? img.attr("abs:data-src") : null;
                    String summary = lede != null ? lede.text() : "";
                    String source = press != null ? press.text() : "";

                    // News 객체로 만들어서 저장
                    News news = News.builder()
                            .title(title)
                            .content(summary)
                            .newsImgUrl(newsImgUrl)
                            .newsCategory(category)
                            .source(source)
                            .url(naverUrl)
                            .originalUrl(naverUrl)
                            .publishedAt(LocalDateTime.now())
                            .crawledAt(LocalDateTime.now())
                            .build();

                    newsList.add(news);
                }
            }

            newsRepository.saveAll(newsList);

            log.info("네이버 뉴스 스크래핑 완료 - URL: {}, 조회 개수: {}", url, newsList.size());

            // 비동기로 각 기사의 원문 크롤링
            newsList.forEach(
                    news -> newsCrawlingService.crawlFullContent(news.getId())
            );

        } catch (Exception e) {
            log.error("네이버 뉴스 스크래핑 중 오류 발생 - URL: {}", url, e);
            throw new RuntimeException("뉴스 조회 중 오류 발생", e);
        }

        return newsList.stream()
                .map(n -> NewsResponseDTO.SingleNewsDTO.builder()
                        .newsId(n.getId())
                        .title(n.getTitle())
                        .url(n.getUrl())
                        .originalUrl(n.getOriginalUrl())
                        .build()
                )
                .collect(Collectors.toList());
    }

    public List<TermResponseDTO.SingleTermResultDTO> viewNewsTerms(Long newsId) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.NEWS_NOT_FOUND));

        List<NewsTerm> newsTermList = newsTermRepository.findAllByNews(news);

        return newsTermList.stream()
                .map(nt -> TermResponseDTO.SingleTermResultDTO.builder()
                        .termId(nt.getTerm().getId())
                        .termName(nt.getTerm().getTermName())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
