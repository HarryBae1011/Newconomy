package com.newconomy.quiz.service;

import com.newconomy.news.domain.News;
import com.newconomy.news.repository.NewsRepository;
import com.newconomy.quiz.converter.QuizConverter;
import com.newconomy.quiz.domain.Quiz;
import com.newconomy.quiz.dto.QuizRequestDTO;
import com.newconomy.quiz.dto.QuizResponseDTO;
import com.newconomy.quiz.repository.QuizRepository;
import com.newconomy.term.domain.Term;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class QuizGenerateService {

    private final QuizRepository quizRepository;
    private final NewsRepository newsRepository;
    private final TermRepository termRepository;
    private final QuizService quizService;
    private final WebClient webClient;

    @Async
    public void generateQuiz(Long newsId) {

        if(quizRepository.existsByNewsId(newsId)){
            return;
        }

        News news = newsRepository.findById(newsId).orElseThrow(() ->
                new EntityNotFoundException("뉴스를 찾을 수 없습니다"));

        webClient.post()
                .uri("/api/quiz/generate")
                .bodyValue(new QuizRequestDTO.QuizGenerateRequestDTO(news.getId(), news.getFullContent()))
                .retrieve()
                .bodyToMono(QuizResponseDTO.QuizListResponseDto.class)
                .subscribe(response -> {
                    if (response != null && response.getQuizList() != null) {
                        quizService.saveQuizzesWithNews(response.getQuizList(), newsId);
                    }
                },error -> {
                    log.error("LLM 호출 중 에러 발생 (뉴스 ID: {}): {}", newsId, error.getMessage());
                });
    }

    @Async
    public void generateQuizByTerm(String batchId){
        List<Term> terms = termRepository.find4RandomTerms();
        List<QuizRequestDTO.QuizGenerateByTermRequestDTO> requestDTO = terms.stream().
                map(QuizConverter::toQuizGenerateByTermDTO).toList();

        Map<String, Object> requestBody = Map.of("terms",requestDTO);
        webClient.post()
                .uri("/api/quiz/generate/terms")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(QuizResponseDTO.QuizListResponseDto.class)
                .subscribe(response -> {
                    if (response != null && response.getQuizList() != null) {
                        quizService.saveQuizzesWithTerms(response.getQuizList(), batchId);
                    }
                },error -> {
                    log.error("LLM 호출 중 에러 발생: {}", error.getMessage());
                });
    }
}
