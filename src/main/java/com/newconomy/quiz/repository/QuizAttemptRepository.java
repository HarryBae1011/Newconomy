package com.newconomy.quiz.repository;

import com.newconomy.member.domain.Member;
import com.newconomy.quiz.domain.QuizAttempt;
import jakarta.persistence.Entity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizAttemptRepository extends JpaRepository <QuizAttempt, Long> {

    @EntityGraph(attributePaths = {"quiz"})
    Page<QuizAttempt> findByMember(Member member, Pageable pageable);

    @EntityGraph(attributePaths = {"quiz"})
    @Query("select qa from QuizAttempt qa " +
            "where qa.member.id = :memberId " +
            "and qa.isCorrect = false ")
    Page<QuizAttempt> findWrongQuizzesByMemberId(@Param("memberId") Long memberId, Pageable pageable);
}
