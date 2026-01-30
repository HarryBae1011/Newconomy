package com.newconomy.quiz.repository;

import com.newconomy.quiz.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    @Query("select q from Quiz q left join fetch q.quizOptionList where q.news.id = :newsId")
    List<Quiz> findByNewsId(@Param("newsId") Long newsId);

    boolean existsByNewsId(Long newsId);
}
