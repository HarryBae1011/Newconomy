package com.newconomy.member.service;

import com.newconomy.global.error.exception.handler.GeneralHandler;
import com.newconomy.global.response.status.ErrorStatus;
import com.newconomy.member.domain.Member;
import com.newconomy.member.dto.MemberRequestDTO;
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
                .profileImage(member.getProfileImage())
                .level(member.getLevel())
                .total_points(member.getTotalPoints())
                .build();
    }

    @Transactional
    public MemberResponseDTO.MemberProfileDTO changeProfile(Long memberId, MemberRequestDTO.ProfileChangeRequestDTO request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 닉네임을 변경하는 경우, 닉네임 중복 체크
        if (request.getNickName() != null && !request.getNickName().equals(member.getNickname())) {
            if (memberRepository.existsByNickname(request.getNickName())) {
                throw new GeneralHandler(ErrorStatus.NICKNAME_ALREADY_EXISTS);
            }
        }

        // 프로필 업데이트
        member.updateProfile(request.getName(), request.getNickName(), request.getProfileImage());
        memberRepository.save(member);

        return MemberResponseDTO.MemberProfileDTO.builder()
                .name(member.getName())
                .nickName(member.getNickname())
                .profileImage(member.getProfileImage())
                .level(member.getLevel())
                .total_points(member.getTotalPoints())
                .build();
    }
}
