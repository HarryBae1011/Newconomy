package com.newconomy.member.repository;

import com.newconomy.member.domain.Member;
import com.newconomy.member.domain.MemberTerm;
import com.newconomy.term.domain.Term;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberTermRepository extends JpaRepository<MemberTerm, Long> {
    Optional<MemberTerm> findByMemberAndTerm(Member member, Term term);
    boolean existsByMemberAndTerm(Member member, Term term);
}
