package com.newconomy.news.crawling;

import com.newconomy.global.common.DateUtils;
import com.newconomy.news.domain.News;
import com.newconomy.news.dto.NewsResponseDTO;
import com.newconomy.news.enums.NewsCategory;
import com.newconomy.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchClient searchClient;
    private final NewsRepository newsRepository;

    public NewsResponseDTO.NewsItemsResponseDTO searchNews(String query, Integer display) {
        NewsResponseDTO.NewsItemsResponseDTO newsItemsResponseDTO = searchClient.searchNews(query, display);

        //기존 데이터 삭제
        newsRepository.deleteAll();

        //서비스 내부 News 엔티티로 변환후 저장
        List<News> newsList = newsItemsResponseDTO.getItems().stream()
                .map(i -> News.builder()
                        .title(i.getTitle())
                        .content(i.getDescription())
                        .newsCategory(NewsCategory.toNewsCategory(query))
                        .source("NAVER")
                        .originalUrl(i.getOriginallink())
                        .publishedAt(DateUtils.parseRFC1123(i.getPubDate()))
                        .crawledAt(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());

        newsRepository.saveAll(newsList);
        return newsItemsResponseDTO;
    }
}

