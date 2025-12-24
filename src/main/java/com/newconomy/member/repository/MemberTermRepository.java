package com.newconomy.member.repository;

import com.newconomy.member.domain.Member;
import com.newconomy.member.domain.MemberTerm;
import com.newconomy.term.domain.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberTermRepository extends JpaRepository<MemberTerm, Long> {
    Optional<MemberTerm> findByMemberAndTerm(Member member, Term term);
    boolean existsByMemberAndTerm(Member member, Term term);

    @Query("select mt from MemberTerm mt join fetch mt.term where mt.member = :member")
    List<MemberTerm> findAllByMember(@Param("member") Member member);
}
