package com.newconomy.quiz.controller;

import com.newconomy.global.response.ApiResponse;
import com.newconomy.quiz.domain.Quiz;
import com.newconomy.quiz.dto.QuizRequestDTO;
import com.newconomy.quiz.service.QuizGenerateService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz")
public class QuizController {

    private final QuizGenerateService quizGenerateService;

    @Operation(summary = "해당 뉴스의 퀴즈 생성", description = "퀴즈 생성 API, fastapi서버의 upstage api를 호출합니다 / QuizId를 List로 반환")
    @PostMapping("/generate/{newsId}")
    public ApiResponse<List<Long>> generateQuiz(@PathVariable("newsId") Long newsId) {
        List<Long> quizIds = quizGenerateService.generateQuiz(newsId);
        return ApiResponse.onSuccess(quizIds);
    }
}
