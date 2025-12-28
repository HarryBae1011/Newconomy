package com.newconomy.quiz.controller;

import com.newconomy.global.response.ApiResponse;
import com.newconomy.quiz.domain.Quiz;
import com.newconomy.quiz.dto.QuizRequestDTO;
import com.newconomy.quiz.service.QuizGenerateService;
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

    @PostMapping("/generate/{newsId}")
    public ApiResponse<String> generateQuiz(@PathVariable("newsId") Long newsId) {
        quizGenerateService.generateQuiz(newsId);
        return ApiResponse.onSuccess("퀴즈가 성공적으로 만들어졌습니다");
    }
}
