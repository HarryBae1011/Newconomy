package com.newconomy.member.auth;

import com.newconomy.global.response.ApiResponse;
import com.newconomy.member.auth.dto.AuthRequestDTO;
import com.newconomy.member.auth.dto.AuthResponseDTO;
import com.newconomy.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "인증 컨트롤러")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

/*    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal Long memberId) {
        return ResponseEntity.ok(Map.of(
                "memberId", memberId,
                "message", "인증된 사용자입니다."
        ));
    }*/

    @PostMapping("/signup")
    @Operation(summary = "유저 비밀번호 회원가입", description = "비밀번호로 회원가입시 provider와 providerId는 null로 설정")
    public ApiResponse<AuthResponseDTO.SignUpResponseDTO> join(@RequestBody AuthRequestDTO.SignUpRequestDTO request) {
        Member joinedMember = authService.joinMember(request);
        return ApiResponse.onSuccess(AuthResponseDTO.SignUpResponseDTO.builder()
                .memberId(joinedMember.getId())
                .memberEmail(joinedMember.getEmail())
                .build());
    }

    @PostMapping("/login")
    @Operation(summary = "유저 로그인", description = "유저 로그인 성공시 JWT 토큰을 발급")
    public ApiResponse<AuthResponseDTO.LoginResponseDTO> login(@RequestBody AuthRequestDTO.LoginRequestDTO request) {
        AuthResponseDTO.LoginResponseDTO loginResponseDTO = authService.login(request);
        return ApiResponse.onSuccess(loginResponseDTO);
    }
}
