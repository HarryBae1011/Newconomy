package com.newconomy.member.repository;

import com.newconomy.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByProviderAndProviderId(Member.SocialProvider provider, String providerId);
    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);
}
