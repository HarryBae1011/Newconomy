package com.newconomy.news.scheduler;

import com.newconomy.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewsCrawlScheduler {
    private final NewsService newsService;

    @Scheduled(cron = "0 0 * * * *")
    public void scheduledTask() {
        log.info("Execute Scheduler - Now: {}", LocalDateTime.now());

        if (newsService.searchHeadLineNews("경제") == null) {
            log.warn("Failed to update news: 경제");
        }

        if (newsService.searchHeadLineNews("금융") == null) {
            log.warn("Failed to update news: 금융");
        }

        if (newsService.searchHeadLineNews("증권") == null) {
            log.warn("Failed to update news: 증권");
        }

        if (newsService.searchHeadLineNews("산업/재계") == null) {
            log.warn("Failed to update news: 산업/재계");
        }

        if (newsService.searchHeadLineNews("중기/벤처") == null) {
            log.warn("Failed to update news: 중기/벤처");
        }

        if (newsService.searchHeadLineNews("부동산") == null) {
            log.warn("Failed to update news: 부동산");
        }

        if (newsService.searchHeadLineNews("글로벌 경제") == null) {
            log.warn("Failed to update news: 글로벌 경제");
        }

        if (newsService.searchHeadLineNews("생활경제") == null) {
            log.warn("Failed to update news: 생활경제");
        }

        if (newsService.searchHeadLineNews("경제 일반") == null) {
            log.warn("Failed to update news: 경제 일반");
        }

    }
}
