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
    private final NewsTermRepository newsTermRepository;
    private final TermRepository termRepository;
    private final WebClient webClient;

    @Async
    public void generateNewsTerm(Long newsId){
        News news = newsRepository.findById(newsId).orElseThrow(() ->
                new EntityNotFoundException("뉴스를 찾을 수 없습니다"));

        Map<String, String> body = Map.of("content", news.getFullContent());

        log.info("llm호출 시작");
        webClient.post()
                .uri("/api/news-term/generate")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(NewsResponseDTO.NewsTermGenerateListDTO.class)
                .toFuture()
                .thenAccept(response -> {
                    if(response != null){
                        saveNewsTerms(response, news);
                    }
                })
                        .exceptionally(ex->{
                            log.error("llm 호출 중 에러 발생:");
                            return null;
                        });
        log.info("llm호출 종료");

    }
    @Transactional
    public void saveNewsTerms(NewsResponseDTO.NewsTermGenerateListDTO responseDTO, News news){
        Long start = System.currentTimeMillis();

        responseDTO.getNewsTermGenerateList().forEach(response -> {
            // 1. 용어 자체 존재 여부 확인 및 저장
            Term term = termRepository.findByTermName(response.getTermName())
                    .orElseGet(() -> termRepository.save(TermConverter.toTerm(response)));

            boolean isAlreadyExists = newsTermRepository.existsByNewsAndTerm(news, term);

            if (!isAlreadyExists) {
                newsTermRepository.save(NewsTermConverter.toNewsTerm(response, term, news));
                log.info("새로운 용어 연결 저장: {}", term.getTermName());
            } else {
                log.info("이미 연결된 용어라 건너뜁니다: {}", term.getTermName());
            }
        });

        log.info("저장 완료 소요 시간: {}ms", System.currentTimeMillis() - start);
    }
}
