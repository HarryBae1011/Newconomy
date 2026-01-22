package com.newconomy.global.security.oauth;

import com.newconomy.global.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        // OAuth2 로그인 성공시 우리 서버에서 클라이언트에게 JWT 토큰 발급
        String token = jwtTokenProvider.generateToken(
                oAuth2User.getMember().getId(),
                oAuth2User.getMember().getEmail(),
                oAuth2User.getMember().getRole().name()
        );

        String redirectUrl = UriComponentsBuilder.fromUriString(
                //"http://localhost:3000/oauth/callback"
                        "https://newconomy.vercel.app/oauth/callback")
                .queryParam("token", token)
                .build()
                .toUriString();

        log.info("OAuth2 login success. Redirecting to: {}", redirectUrl);
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
