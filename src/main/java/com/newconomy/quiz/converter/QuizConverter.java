package com.newconomy.quiz.converter;

import com.newconomy.quiz.domain.Quiz;
import com.newconomy.quiz.domain.QuizOption;
import com.newconomy.quiz.dto.QuizRequestDTO;
import com.newconomy.quiz.dto.QuizResponseDTO;
import com.newconomy.quiz.enums.QuizType;

import java.util.ArrayList;
import java.util.List;

public class QuizConverter {
    public static Quiz toQuizEntity(QuizResponseDTO.QuizGenerateResponseDTO dto) {
        Quiz quiz = Quiz.builder()
                .quizType(QuizType.valueOf(dto.getQuizType()))
                .question(dto.getQuestion())
                .correctAnswer(dto.getCorrectAnswer())
                .explanation(dto.getExplanation())
                .difficultyLevel(dto.getDifficultyLevel())
                .quizOptionList(new ArrayList<>()) // 가변 리스트로 초기화
                .build();

        // 옵션 변환 및 연관관계 설정
        if (dto.getQuizOptionList() != null) {
            List<QuizOption> options = dto.getQuizOptionList().stream()
                    .map(optionDto -> toQuizOptionEntity(optionDto, quiz))
                    .toList();
            quiz.getQuizOptionList().addAll(options);
        }

        return quiz;
    }

    public static QuizOption toQuizOptionEntity(QuizResponseDTO.QuizOptionResposneDTO dto, Quiz quiz) {
        return QuizOption.builder()
                .optionText(dto.getOptionText())
                .optionOrder(dto.getOptionOrder())
                .isCorrect(dto.isCorrect())
                .quiz(quiz) // 부모 엔티티 참조 설정 (FK)
                .build();
    }
}
