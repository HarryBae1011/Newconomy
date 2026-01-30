package com.newconomy.quiz.service;

import com.newconomy.global.error.exception.GeneralException;
import com.newconomy.global.response.status.ErrorStatus;
import com.newconomy.member.domain.Member;
import com.newconomy.member.repository.MemberRepository;
import com.newconomy.news.domain.News;
import com.newconomy.news.repository.NewsRepository;
import com.newconomy.quiz.converter.QuizConverter;
import com.newconomy.quiz.domain.Quiz;
import com.newconomy.quiz.domain.QuizAttempt;
import com.newconomy.quiz.dto.QuizRequestDTO;
import com.newconomy.quiz.dto.QuizResponseDTO;
import com.newconomy.quiz.repository.QuizAttemptRepository;
import com.newconomy.quiz.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizService {

    private final QuizRepository quizRepository;
    private final MemberRepository memberRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final NewsRepository newsRepository;


    public Page<QuizResponseDTO.SubmitResultDTO> getQuizAttempts(Long memberId, Pageable pageable){
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        Page<QuizAttempt> quizAttempts = quizAttemptRepository.findByMember(member, pageable);
        return quizAttempts.map(QuizConverter::toSubmitResultDTO);
    }

    public Page<QuizResponseDTO.SubmitResultDTO> getWrongQuizzes(Long memberId, Pageable pageable){
        Page<QuizAttempt> quizAttempts = quizAttemptRepository.findWrongQuizzesByMemberId(memberId, pageable);
        return quizAttempts.map(QuizConverter::toSubmitResultDTO);
    }

    public List<QuizResponseDTO.QuizGenerateResponseDTO> getQuizzesWithNews(Long newsId){
        List<Quiz> quizzes = quizRepository.findByNewsId(newsId);
        return quizzes.stream().map(quiz -> QuizConverter.toQuizDTO(quiz)).toList();
    }

    @Transactional
    public QuizResponseDTO.SubmitResultDTO submitAnswer(Long quizId, Long memberId, QuizRequestDTO.SubmitDTO request){
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(
                () -> new GeneralException(ErrorStatus.QUIZ_NOT_FOUND));

        boolean isCorrect = request.getMemberAnswer().equals(quiz.getCorrectAnswer());

        QuizAttempt quizAttempt = QuizAttempt.builder()
                .member(member)
                .quiz(quiz)
                .memberAnswer(request.getMemberAnswer())
                .attemptedAt(LocalDateTime.now())
                .isCorrect(isCorrect)
                .build();
        QuizAttempt saved = quizAttemptRepository.save(quizAttempt);
        return QuizConverter.toSubmitResultDTO(saved);
    }

    @Transactional
    public void saveQuizzesWithNews(List<QuizResponseDTO.QuizGenerateResponseDTO> quizList, Long newsId) {
        News news = newsRepository.getReferenceById(newsId); //ID만 필요하기 때문
        List<Quiz> entities = quizList.stream()
                .map(dto -> QuizConverter.toQuizEntity(dto, news, null)).toList();
        quizRepository.saveAll(entities);
    }
}
