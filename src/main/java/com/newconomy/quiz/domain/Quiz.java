package com.newconomy.quiz.domain;

import com.newconomy.global.common.BaseEntity;
import com.newconomy.news.domain.News;
import com.newconomy.quiz.enums.QuizType;
import com.newconomy.term.domain.Term;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Quiz extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private News news;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id")
    private Term term;

    @Enumerated(value = EnumType.STRING)
    private QuizType quizType;

    @Lob
    @Column(nullable = false)
    private String question;

    @NotBlank
    private String correctAnswer;

    private String explanation;
    private int difficultyLevel;

    @Builder.Default
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizOption> quizOptionList = new ArrayList<>();


}
