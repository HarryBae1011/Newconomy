package com.newconomy.news.domain;

import com.newconomy.global.common.BaseEntity;
import com.newconomy.term.domain.Term;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class NewsTerm extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private News news;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id")
    private Term term;

    // 뉴스 기사 전체 기준 위치
    private int startIndex;
    private int endIndex;

    private String contextSentence;
}
