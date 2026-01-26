package com.newconomy.member.controller;

import com.newconomy.global.response.ApiResponse;
import com.newconomy.member.dto.MemberRequestDTO;
import com.newconomy.member.dto.MemberResponseDTO;
import com.newconomy.member.service.MemberService;
import com.newconomy.quiz.dto.QuizResponseDTO;
import com.newconomy.quiz.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import com.newconomy.term.dto.TermResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "멤버 컨트롤러")
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final QuizService quizService;

    @GetMapping("/me/profile")
    @Operation(summary = "본인 회원 정보 조회")
    public ApiResponse<MemberResponseDTO.MemberProfileDTO> getMemberProfile(
            @AuthenticationPrincipal Long memberId) {
        MemberResponseDTO.MemberProfileDTO memberProfileDTO = memberService.getMember(memberId);
        return ApiResponse.onSuccess(memberProfileDTO);
    }

    @PatchMapping("/me/profile/edit")
    @Operation(summary = "본인 회원 정보 수정")
    public ApiResponse<MemberResponseDTO.MemberProfileDTO> changeMemberProfile(
            @AuthenticationPrincipal Long memberId,
            @RequestBody MemberRequestDTO.ProfileChangeRequestDTO request) {
        MemberResponseDTO.MemberProfileDTO memberProfileDTO = memberService.changeProfile(memberId, request);
        return ApiResponse.onSuccess(memberProfileDTO);
    }

    //member 퀴즈 관련
    @Operation(summary = "퀴즈 풀이 기록 조회")
    @GetMapping("/me/quiz-attempts")
    public ApiResponse<Page<QuizResponseDTO.SubmitResultDTO>> getQuizAttempts(@AuthenticationPrincipal Long memberId,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        return ApiResponse.onSuccess(quizService.getQuizAttempts(memberId,pageable));
    }

    @Operation(summary = "틀린 퀴즈 목록 조회")
    @GetMapping("/me/wrong-quizzes")
    public ApiResponse<Page<QuizResponseDTO.SubmitResultDTO>> getWrongQuizzes(@AuthenticationPrincipal Long memberId,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        return ApiResponse.onSuccess(quizService.getWrongQuizzes(memberId,pageable));
    }

    @GetMapping("/{memberId}/term")
    @Operation(summary = "본인이 공부한 경제 용어 조회", description = "사용자가 조회한 적이 있는 경제용어 목록 조회")
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
