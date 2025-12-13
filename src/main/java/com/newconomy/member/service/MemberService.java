package com.newconomy.member.service;

import com.newconomy.global.error.exception.handler.GeneralHandler;
import com.newconomy.global.response.status.ErrorStatus;
import com.newconomy.member.domain.Member;
import com.newconomy.member.dto.MemberResponseDTO;
import com.newconomy.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponseDTO.MemberProfileDTO getMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));

        return MemberResponseDTO.MemberProfileDTO.builder()
                .name(member.getName())
                .nickName(member.getNickname())
                .level(member.getLevel())
                .total_points(member.getTotalPoints())
                .build();
    }
}
