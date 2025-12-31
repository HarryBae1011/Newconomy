package com.newconomy.quiz.service;

import com.newconomy.news.domain.News;
import com.newconomy.news.repository.NewsRepository;
import com.newconomy.quiz.converter.QuizConverter;
import com.newconomy.quiz.domain.Quiz;
import com.newconomy.quiz.dto.QuizRequestDTO;
import com.newconomy.quiz.dto.QuizResponseDTO;
import com.newconomy.quiz.repository.QuizRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class QuizGenerateService {

    private final QuizRepository quizRepository;
    private final NewsRepository newsRepository;
    private final WebClient webClient;

    public List<Long> generateQuiz(Long newsId) {
        News news = newsRepository.findById(newsId).orElseThrow(() ->
                new EntityNotFoundException("뉴스를 찾을 수 없습니다"));

        QuizResponseDTO.QuizListResponseDto responseDto = webClient.post()
                .uri("/api/quiz/generate")
                .bodyValue(new QuizRequestDTO.QuizRequestDto(news.getId(), news.getFullContent()))
                .retrieve()
                .bodyToMono(QuizResponseDTO.QuizListResponseDto.class)
                .block();

        List<QuizResponseDTO.QuizGenerateResponseDTO> quizList = responseDto.getQuizList();
        List<Quiz> saved = quizRepository.saveAll(quizList.stream().map(QuizConverter::toQuizEntity)
                .toList());
        log.info("뉴스 ID {}로부터 {}개의 퀴즈가 성공적으로 생성 및 저장되었습니다.", newsId, saved.size());
        return saved.stream()
                .map(Quiz::getId)
                .collect(Collectors.toList());

    }
}
