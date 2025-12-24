package com.newconomy.member.controller;

import com.newconomy.global.response.ApiResponse;
import com.newconomy.member.dto.MemberRequestDTO;
import com.newconomy.member.dto.MemberResponseDTO;
import com.newconomy.member.service.MemberService;
import com.newconomy.term.dto.TermResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "멤버 컨트롤러")
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

    @GetMapping("/{memberId}/term")
    public ApiResponse<TermResponseDTO.TermResultListDTO> viewLearnedTerm(
            @PathVariable Long memberId
    ) {
        List<TermResponseDTO.SingleTermResultDTO> learnedTerm = memberService.getLearnedTerm(memberId);
        return ApiResponse.onSuccess(
                TermResponseDTO.TermResultListDTO.builder()
                        .terms(learnedTerm)
                        .build());
    }
}
