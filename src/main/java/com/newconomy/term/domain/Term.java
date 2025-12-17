package com.newconomy.term.domain;

import com.newconomy.global.common.BaseEntity;
import com.newconomy.term.enums.TermCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Term {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String termName;

    @Lob
    private String simpleExplanation;

    @Lob
    private String detailedExplanation;

    @Enumerated(EnumType.STRING)
    private TermCategory termCategory;

    private Integer difficultyLevel;
}
