package com.newconomy.news.crawling;

import com.newconomy.global.error.exception.handler.GeneralHandler;
import com.newconomy.global.response.status.ErrorStatus;
import com.newconomy.news.domain.News;
import com.newconomy.news.repository.NewsRepository;
import com.newconomy.term.service.TermExtractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsCrawlingService {

    private final NewsRepository newsRepository;
    private final TermExtractService termExtractService;

    // 뉴스 기사 원문 크롤링 (비동기, 딜레이)
    @Async
    @Transactional
    public void crawlFullContent(Long newsId, String url) {
        try {
            Thread.sleep(2000);

            // 기사 원문 페이지 HTML 가져오기
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(5000)
                    .get();

            // 네이버 뉴스 본문 추출
            String content = extractNaverNewsContent(doc);

            if (content != null) {
                // 뉴스 본문에서 경제 용어 추출
                termExtractService.extract(newsId, content);

                News news = newsRepository.findById(newsId)
                        .orElseThrow(() -> new GeneralHandler(ErrorStatus.NEWS_NOT_FOUND));

                news.updateFullContent(content);
                newsRepository.save(news);
            }

        } catch (Exception e) {
            News news = newsRepository.findById(newsId)
                    .orElseThrow(() -> new GeneralHandler(ErrorStatus.NEWS_NOT_FOUND));

            log.error("크롤링 실패: {}", news.getOriginalUrl(), e);
        }
    }

    private String extractNaverNewsContent(Document doc) {
        // 네이버 뉴스 플랫폼의 표준 셀렉터들
        String[] selectors = {
                "article#dic_area",           // 최신 (2020년 이후)
                "div#articleBodyContents",    // 구버전 (2019년 이전)
                "div#newsEndContents"         // 또 다른 구버전
        };

        for (String selector : selectors) {
            Element article = doc.selectFirst(selector);

            if (article != null) {

                // 불필요한 요소 제거
                article.select("script, style, iframe").remove();
                article.select(".ad_area, .ad, ._GRID_TEMPLATE_COLUMN_ASIDE").remove();
                article.select("em.img_desc").remove(); // 이미지 설명
                article.select("span.end_photo_org").remove(); // 사진 출처

                String text = article.text()
                        .replaceAll("\\s+", " ") // 여러 공백을 하나로
                        .trim();

                // 네이버 뉴스 특유의 불필요한 텍스트 제거
                text = text.replaceAll("^// flash.*?// ]]>", "")
                        .replaceAll("\\[.*?기자\\]", "")
                        .replaceAll("무단전재.*?금지", "")
                        .trim();

                if (text.length() > 50) {
                    return text;
                }
            }
        }

        log.warn("모든 셀렉터 실패");
        return null;
    }
}
