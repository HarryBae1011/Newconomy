package com.newconomy.quiz.converter;

import com.newconomy.news.domain.News;
import com.newconomy.quiz.domain.Quiz;
import com.newconomy.quiz.domain.QuizAttempt;
import com.newconomy.quiz.domain.QuizOption;
import com.newconomy.quiz.dto.QuizRequestDTO;
import com.newconomy.quiz.dto.QuizResponseDTO;
import com.newconomy.quiz.enums.QuizType;
import com.newconomy.term.domain.Term;

import java.util.ArrayList;
import java.util.List;

public class QuizConverter {
    public static Quiz toQuizEntity(QuizResponseDTO.QuizGenerateResponseDTO dto, News news, String batchId) {
        Quiz quiz = Quiz.builder()
                .news(news)
                .batchId(batchId)
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

    public static QuizResponseDTO.QuizGenerateResponseDTO toQuizDTO(Quiz quiz){
        return QuizResponseDTO.QuizGenerateResponseDTO.builder()
                .id(quiz.getId())
                .quizType(quiz.getQuizType().toString())
                .difficultyLevel(quiz.getDifficultyLevel())
                .correctAnswer(quiz.getCorrectAnswer())
                .explanation(quiz.getExplanation())
                .question(quiz.getQuestion())
                .quizOptionList(quiz.getQuizOptionList().stream().map(QuizConverter::toQuizOptionDTO).toList())
                .build();
    }

    public static QuizOption toQuizOptionEntity(QuizResponseDTO.QuizOptionResposneDTO dto, Quiz quiz) {
        return QuizOption.builder()
                .optionText(dto.getOptionText())
                .optionOrder(dto.getOptionOrder())
                .isCorrect(dto.isCorrect())
                .quiz(quiz) // 부모 엔티티 참조 설정 (FK)
                .build();
    }

    public static QuizResponseDTO.QuizOptionResposneDTO toQuizOptionDTO(QuizOption quizOption){
        return QuizResponseDTO.QuizOptionResposneDTO.builder()
                .optionOrder(quizOption.getOptionOrder())
                .optionText(quizOption.getOptionText())
                .isCorrect(quizOption.isCorrect())
                .build();
    }

    public static QuizResponseDTO.SubmitResultDTO toSubmitResultDTO(QuizAttempt quizAttempt){
        Quiz quiz = quizAttempt.getQuiz();
        return QuizResponseDTO.SubmitResultDTO.builder()
                .quizId(quiz.getId())
                .quizAttemptId(quizAttempt.getId())
                .correctAnswer(quiz.getCorrectAnswer())
                .explanation(quiz.getExplanation())
                .isCorrect(quizAttempt.isCorrect())
                .memberAnswer(quizAttempt.getMemberAnswer())
                .build();
    }

    public static QuizRequestDTO.QuizGenerateByTermRequestDTO toQuizGenerateByTermDTO(Term term){
        return QuizRequestDTO.QuizGenerateByTermRequestDTO.builder()
                .termId(term.getId())
                .termName(term.getTermName())
                .simpleExplanation(term.getSimpleExplanation())
                .detailedExplanation(term.getDetailedExplanation())
                .build();
    }
}
