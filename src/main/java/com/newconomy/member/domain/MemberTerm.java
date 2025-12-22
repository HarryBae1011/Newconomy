package com.newconomy.member.domain;

import com.newconomy.term.domain.Term;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberTerm {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id")
    private Term term;

    private int masteryLevel;
    private int viewCount;
    private boolean isSaved;
    private LocalDateTime firstLearnedAt;
    private LocalDateTime lastReviewedAt;

    public void updateTermStudy(LocalDateTime lastReviewedAt) {
        this.viewCount++;
        this.lastReviewedAt = lastReviewedAt;
    }

    public void bookmarkTerm() {
        this.isSaved = true;
    }
}
