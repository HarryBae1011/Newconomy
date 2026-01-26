package com.newconomy.news.service;

import com.newconomy.news.converter.NewsTermConverter;
import com.newconomy.news.domain.News;
import com.newconomy.news.dto.NewsResponseDTO;
import com.newconomy.news.repository.NewsRepository;
import com.newconomy.news.repository.NewsTermRepository;
import com.newconomy.term.converter.TermConverter;
import com.newconomy.term.domain.Term;
import com.newconomy.term.dto.TermResponseDTO;
import com.newconomy.term.repository.TermRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsTermGenerateService {
    private final NewsRepository newsRepository;
    private final NewsService newsService;
    private final WebClient webClient;

    @Async
    public void generateNewsTerm(Long newsId){
        News news = newsRepository.findById(newsId).orElseThrow(() ->
                new EntityNotFoundException("뉴스를 찾을 수 없습니다"));

        Map<String, String> body = Map.of("content", news.getFullContent());

        webClient.post()
                .uri("/api/news-term/generate")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(NewsResponseDTO.NewsTermGenerateListDTO.class)
                .subscribe(response -> {
                    if(response!=null){
                        newsService.saveNewsTerms(response, news);
                    }
                });
    }
}
