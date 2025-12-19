package com.newconomy.member.controller;

import com.newconomy.global.response.ApiResponse;
import com.newconomy.member.dto.MemberRequestDTO;
import com.newconomy.member.dto.MemberResponseDTO;
import com.newconomy.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me/profile")
    public ApiResponse<MemberResponseDTO.MemberProfileDTO> getMemberProfile(
            @AuthenticationPrincipal Long memberId) {
        MemberResponseDTO.MemberProfileDTO memberProfileDTO = memberService.getMember(memberId);
        return ApiResponse.onSuccess(memberProfileDTO);
    }

    @PatchMapping("/me/profile/edit")
    public ApiResponse<MemberResponseDTO.MemberProfileDTO> changeMemberProfile(
            @AuthenticationPrincipal Long memberId,
            @RequestBody MemberRequestDTO.ProfileChangeRequestDTO request) {
        MemberResponseDTO.MemberProfileDTO memberProfileDTO = memberService.changeProfile(memberId, request);
        return ApiResponse.onSuccess(memberProfileDTO);
    }
}
