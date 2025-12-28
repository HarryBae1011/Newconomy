package com.newconomy.quiz.dto;

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
    public static class QuizResponseDto{
        String quizType;
        String question;
        String correctAnswer;
        String explanation;
        int difficultyLevel;
        List<QuizOptionResposneDto> quizOptionList;
    }

    public static class QuizOptionResposneDto{
        String optionText;
        int optionOrder;
        boolean isCorrect;
    }
}
