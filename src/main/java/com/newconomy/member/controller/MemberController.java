package com.newconomy.member.controller;

import com.newconomy.global.response.ApiResponse;
import com.newconomy.member.dto.MemberResponseDTO;
import com.newconomy.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
