package com.newconomy.quiz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.newconomy.quiz.domain.QuizOption;
import com.newconomy.quiz.enums.QuizType;
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
}
