package com.newconomy.news.crawling;

import com.newconomy.global.common.DateUtils;
import com.newconomy.global.common.HtmlUtils;
import com.newconomy.news.domain.News;
import com.newconomy.news.dto.NewsResponseDTO;
import com.newconomy.news.enums.NewsCategory;
import com.newconomy.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchService {

    private final SearchClient searchClient;
    private final NewsRepository newsRepository;
    private final NewsCrawlingService newsCrawlingService;

    @Transactional
    public NewsResponseDTO.NewsItemsResponseDTO searchNews(String query, Integer display) {
        // 뉴스 기사 목록 (기사 제목, 요약) 수집
        NewsResponseDTO.NewsItemsResponseDTO newsItemsResponseDTO = searchClient.searchNews(query, display);

        List<News> newsList = newsItemsResponseDTO.getItems().stream()
                .filter(i -> {
                    // 네이버 뉴스 URL인지 확인
                    String link = i.getLink();
                    boolean isNaverNews = link.contains("n.news.naver.com") ||
                            link.contains("news.naver.com");

                    if (!isNaverNews) {
                        log.warn("네이버 뉴스가 아님. 스킵: {}", link);
                    }

                    return isNaverNews;
                })
                .limit(5)
                .map(i -> News.builder()
                        .title(HtmlUtils.removeHtmlTags(i.getTitle()))
                        .content(HtmlUtils.removeHtmlTags(i.getDescription()))
                        // 사용자 검색으로 크롤링된 기사는 CUSTOM 카테고리로 저장
                        .newsCategory(NewsCategory.CUSTOM)
                        .source("NAVER")
                        .url(i.getLink())
                        .originalUrl(i.getOriginallink())
                        .publishedAt(DateUtils.parseRFC1123(i.getPubDate()))
                        .crawledAt(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());

        newsRepository.saveAll(newsList);
        newsRepository.flush();

        // 비동기로 뉴스 기사 원문 크롤링 및 경제 용어 추출
        newsList.forEach(
                news -> newsCrawlingService.crawlFullContent(news.getId(), news.getUrl())
        );
        return newsItemsResponseDTO;
    }
}

