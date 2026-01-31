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
import java.util.Random;
import java.util.UUID;

@Tag(name = "퀴즈 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz")
public class QuizController {

    private final QuizGenerateService quizGenerateService;
    private final QuizService quizService;

    @Operation(summary = "해당 뉴스의 퀴즈 생성", description = "퀴즈 생성 API, fastapi서버의 upstage api를 호출합니다")
    @PostMapping("/generate/{newsId}")
    public ApiResponse<String> generateQuiz(@PathVariable("newsId") Long newsId) {
        quizGenerateService.generateQuiz(newsId);
        return ApiResponse.onSuccess("해당 뉴스의 퀴즈 생성이 시작되었습니다");
    }

    @Operation(summary = "해당 뉴스의 퀴즈 조회", description = "퀴즈 조회 API, 해당 뉴스의 퀴즈 list를 조회합니다")
    @GetMapping("/news/{newsId}")
    public ApiResponse<List<QuizResponseDTO.QuizGenerateResponseDTO>> getQuizzesWithNews(@PathVariable("newsId") Long newsId) {
        List<QuizResponseDTO.QuizGenerateResponseDTO> quizzesWithNews = quizService.getQuizzesWithNews(newsId);
        return ApiResponse.onSuccess(quizzesWithNews);
    }

    @Operation(summary = "경제 용어 기반 퀴즈 생성", description = "경제 용어 기반 퀴즈 생성 API, fastapi서버의 upstage api를 호출합니다")
    @PostMapping("/generateByTerm")
    public ApiResponse<String> generateQuiz() {
        String batchId = UUID.randomUUID().toString();
        quizGenerateService.generateQuizByTerm(batchId);
        return ApiResponse.onSuccess("경제 용어 기반 퀴즈 생성이 시작되었습니다, batchId:" + batchId);
    }

    @GetMapping("/term/{batchId}")
    public ApiResponse<List<QuizResponseDTO.QuizGenerateResponseDTO>> getQuizzesWithTerms(@PathVariable("batchId") String batchId) {
        List<QuizResponseDTO.QuizGenerateResponseDTO> quizzesWithTerms = quizService.getQuizzesWithTerms(batchId);
        return ApiResponse.onSuccess(quizzesWithTerms);
    }

    @Operation(summary = "퀴즈 답안 제출")
    @PostMapping("/{quizId}/submit")
    public ApiResponse<QuizResponseDTO.SubmitResultDTO> submitQuiz(@PathVariable Long quizId,
                                          @RequestBody QuizRequestDTO.SubmitDTO submitDto,
                                          @AuthenticationPrincipal Long memberId) {

        return ApiResponse.onSuccess(quizService.submitAnswer(quizId,memberId,submitDto));
    }

}
