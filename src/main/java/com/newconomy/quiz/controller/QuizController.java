package com.newconomy.quiz.controller;

import com.newconomy.global.response.ApiResponse;
import com.newconomy.quiz.domain.Quiz;
import com.newconomy.quiz.dto.QuizRequestDTO;
import com.newconomy.quiz.dto.QuizResponseDTO;
import com.newconomy.quiz.service.QuizGenerateService;
import com.newconomy.quiz.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "퀴즈 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz")
public class QuizController {

    private final QuizGenerateService quizGenerateService;
    private final QuizService quizService;

    @Operation(summary = "해당 뉴스의 퀴즈 생성", description = "퀴즈 생성 API, fastapi서버의 upstage api를 호출합니다")
    @PostMapping("/generate/{newsId}")
    public ApiResponse<List<QuizResponseDTO.QuizGenerateResponseDTO>> generateQuiz(@PathVariable("newsId") Long newsId) {
        List<QuizResponseDTO.QuizGenerateResponseDTO> quizzes = quizGenerateService.generateQuiz(newsId);
        return ApiResponse.onSuccess(quizzes);
    }

    @Operation(summary = "경제 용어 기반 퀴즈 생성", description = "경제 용어 기반 퀴즈 생성 API, fastapi서버의 upstage api를 호출합니다")
    @PostMapping("/generateByTerm")
    public ApiResponse<List<QuizResponseDTO.QuizGenerateResponseDTO>> generateQuizByTerm(){
        List<QuizResponseDTO.QuizGenerateResponseDTO> quizzes = quizGenerateService.generateQuizByTerm();
        return ApiResponse.onSuccess(quizzes);
    }

    @Operation(summary = "퀴즈 답안 제출")
    @PostMapping("/{quizId}/submit")
    public ApiResponse<QuizResponseDTO.SubmitResultDTO> submitQuiz(@PathVariable Long quizId,
                                          @RequestBody QuizRequestDTO.SubmitDTO submitDto,
                                          @AuthenticationPrincipal Long memberId) {

        return ApiResponse.onSuccess(quizService.submitAnswer(quizId,memberId,submitDto));
    }

}
