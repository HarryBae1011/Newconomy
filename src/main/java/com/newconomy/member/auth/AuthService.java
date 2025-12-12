package com.newconomy.member.auth;

import com.newconomy.global.error.exception.handler.GeneralHandler;
import com.newconomy.global.response.status.ErrorStatus;
import com.newconomy.global.security.jwt.JwtTokenProvider;
import com.newconomy.member.auth.dto.AuthRequestDTO;
import com.newconomy.member.auth.dto.AuthResponseDTO;
import com.newconomy.member.domain.Member;
import com.newconomy.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Member joinMember(AuthRequestDTO.SignUpRequestDTO request) {
        // 이메일 중복 체크
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new GeneralHandler(ErrorStatus.DUPLICATE_MEMBER);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Member member = Member.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(encodedPassword)
                .nickname(request.getNickname())
                .role(Member.Role.USER)
                .provider(null)
                .providerId(null)
                .build();

        return memberRepository.save(member);
    }

    @Transactional
    public AuthResponseDTO.LoginResponseDTO login(AuthRequestDTO.LoginRequestDTO request) {
        // 회원가입된 사용자인지 체크
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 소셜 로그인 계정 체크
        if (member.isSocialUser()) {
            throw new GeneralHandler(ErrorStatus.SOCIAL_LOGIN_MEMBER);
        }

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new GeneralHandler(ErrorStatus.WRONG_PASSWORD);
        }

        // JWT 토큰 발급
        String accessToken = jwtTokenProvider.generateToken(member.getId(), member.getEmail(), String.valueOf(member.getRole()));

        return AuthResponseDTO.LoginResponseDTO.builder()
                .accessToken(accessToken)
                .memberId(member.getId())
                .build();
    }
}
