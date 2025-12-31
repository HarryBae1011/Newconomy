package com.newconomy.quiz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.newconomy.quiz.domain.QuizOption;
import com.newconomy.quiz.enums.QuizType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class QuizResponseDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuizListResponseDto{
        List<QuizGenerateResponseDTO> quizList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuizGenerateResponseDTO{
        Long id;
        String quizType;
        String question;
        String correctAnswer;
        String explanation;
        int difficultyLevel;
        List<QuizOptionResposneDTO> quizOptionList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuizOptionResposneDTO{
        String optionText;
        int optionOrder;
        @JsonProperty("isCorrect")
        boolean isCorrect;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "퀴즈 제출 결과 응답 DTO")
    public static class SubmitResultDTO {
        @Schema(description = "제출한 퀴즈의 ID")
        private Long quizId;

        @Schema(description = "생성된 퀴즈 시도 기록 ID")
        private Long quizAttemptId;

        @Schema(description = "정답 여부")
        private boolean isCorrect;

        @Schema(description = "퀴즈의 실제 정답")
        private String correctAnswer;

        @Schema(description = "제출한 정답")
        private String memberAnswer;

        @Schema(description = "퀴즈 해설")
        private String explanation;
    }

}
