package com.newconomy.global.security.oauth;

import com.newconomy.member.domain.Member;
import com.newconomy.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //구글 or 카카오 엑세스 토큰 가져오기 + 엑세스 토큰으로 구글 API 호출해서 사용자 정보 가져오기
        OAuth2User oAuth2User = super.loadUser(userRequest); //Spring Security가 자동처리

        // 구글과 카카오 중 어떤 제공자인지 확인
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 제공자에 맞는 사용자 정보로 변환
        OAuth2UserInfo oAuth2UserInfo = getOAuth2UserInfo(registrationId, attributes);

        // Member DB에 저장
        Member member = saveOrUpdate(oAuth2UserInfo);
        return new CustomOAuth2User(member, attributes);
    }

    private OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) {
            case "kakao" -> new KakaoOAuth2UserInfo(attributes);
            case "google" -> new GoogleOAuth2UserInfo(attributes);
            default -> throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
        };
    }

    private Member saveOrUpdate(OAuth2UserInfo oAuth2UserInfo) {
        Member.SocialProvider provider = Member.SocialProvider.valueOf(oAuth2UserInfo.getProvider().toUpperCase());

        String email = oAuth2UserInfo.getEmail();

        // 이메일 없으면 providerId로 대체
        if (email == null || email.isEmpty()) {
            email = provider.name().toLowerCase() + "_" + oAuth2UserInfo.getProviderId() + "@newconomy.temp";
        }

        String finalEmail = email;

        Member member = memberRepository
                .findByProviderAndProviderId(provider, oAuth2UserInfo.getProviderId())
                .map(existingMember -> {
                    existingMember.updateProfile(
                            oAuth2UserInfo.getName(),
                            existingMember.getNickname(),
                            oAuth2UserInfo.getProfileImage()
                    );
                    return existingMember;
                })
                .orElseGet(() -> Member.builder()
                        .email(finalEmail)
                        .name(oAuth2UserInfo.getName())
                        .profileImage(oAuth2UserInfo.getProfileImage())
                        .provider(provider)
                        .providerId(oAuth2UserInfo.getProviderId())
                        .role(Member.Role.USER)
                        .build());

        return memberRepository.save(member);
    }
}
