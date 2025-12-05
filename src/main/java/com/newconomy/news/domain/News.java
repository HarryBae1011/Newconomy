package com.newconomy.news.domain;

import com.newconomy.global.common.BaseEntity;
import com.newconomy.news.enums.NewsCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class News extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Enumerated(value = EnumType.STRING)
    private NewsCategory newsCategory;

    //언론사 ex) 네이버, 다음 등...
    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String originalUrl;

    @Column(nullable = false)
    private LocalDateTime publishedAt;

    private LocalDateTime crawledAt;

    @Builder.Default
    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NewsTerm> newsTermList = new ArrayList<>();
}
