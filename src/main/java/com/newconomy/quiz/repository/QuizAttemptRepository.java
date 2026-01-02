package com.newconomy.quiz.repository;

import com.newconomy.quiz.domain.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizAttemptRepository extends JpaRepository <QuizAttempt, Long> {

}
