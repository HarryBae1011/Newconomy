package com.newconomy.quiz.controller;

import com.newconomy.global.response.ApiResponse;
import com.newconomy.quiz.domain.Quiz;
import com.newconomy.quiz.dto.QuizRequestDTO;
import com.newconomy.quiz.dto.QuizResponseDTO;
import com.newconomy.quiz.service.QuizGenerateService;
import com.newconomy.quiz.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz")
public class QuizController {

    private final QuizGenerateService quizGenerateService;
    private final QuizService quizService;

    @Operation(summary = "해당 뉴스의 퀴즈 생성", description = "퀴즈 생성 API, fastapi서버의 upstage api를 호출합니다 / QuizId를 List로 반환")
    @PostMapping("/generate/{newsId}")
    public ApiResponse<List<QuizResponseDTO.QuizGenerateResponseDTO>> generateQuiz(@PathVariable("newsId") Long newsId) {
        List<QuizResponseDTO.QuizGenerateResponseDTO> quizzes = quizGenerateService.generateQuiz(newsId);
        return ApiResponse.onSuccess(quizzes);
    }

//    @Operation(summary = "퀴즈 목록 조회")
//    @GetMapping("/quiz")
//    public ApiResponse<Page<QuizResponseDTO.QuizGenerateResponseDTO>> getQuizzes(@RequestParam(defaultValue = "0") int page,
//                                                                                 @RequestParam(defaultValue = "10") int size
//    ) {
//        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
//        return ApiResponse.onSuccess(quizService.getQuizzes(pageable));
//    }

//    @Operation(summary = "퀴즈 상세 조회")
//    @GetMapping("/quiz/{quizId}")
//    public ApiResponse<Object> getQuiz(@PathVariable Long quizId) {
//        // return ApiResponse.onSuccess(quizService.getQuiz(quizId));
//        return ApiResponse.onSuccess("퀴즈 상세 조회: " + quizId);
//    }

    @Operation(summary = "퀴즈 답안 제출")
    @PostMapping("/{quizId}/submit")
    public ApiResponse<QuizResponseDTO.SubmitResultDTO> submitQuiz(@PathVariable Long quizId,
                                          @RequestBody QuizRequestDTO.SubmitDTO submitDto,
                                          @AuthenticationPrincipal Long memberId) {

        return ApiResponse.onSuccess(quizService.submitAnswer(quizId,memberId,submitDto));
    }

}
