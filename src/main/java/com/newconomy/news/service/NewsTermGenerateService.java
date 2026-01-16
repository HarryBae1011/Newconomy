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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NewsTermGenerateService {
    private final NewsRepository newsRepository;
    private final NewsTermRepository newsTermRepository;
    private final TermRepository termRepository;
    private final WebClient webClient;

    @Transactional
    public NewsResponseDTO.NewsTermGenerateListDTO generateNewsTerm(Long newsId){
        News news = newsRepository.findById(newsId).orElseThrow(() ->
                new EntityNotFoundException("뉴스를 찾을 수 없습니다"));

        Map<String, String> body = Map.of("content", news.getFullContent());

        NewsResponseDTO.NewsTermGenerateListDTO responseDTO = webClient.post()
                .uri("/api/news-term/generate")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(NewsResponseDTO.NewsTermGenerateListDTO.class)
                .block();

        responseDTO.getNewsTermGenerateList().stream().forEach(response-> {

            Term term = termRepository.findByTermName(response.getTermName())
                    .orElseGet(()-> termRepository.save(TermConverter.toTerm(response)));

            newsTermRepository.save(NewsTermConverter.toNewsTerm(response,term,news));
        });
        return responseDTO;
    }
}
