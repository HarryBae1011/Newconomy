package com.newconomy.member.domain;

import com.newconomy.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.StringUtils;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 20, nullable = false)
    private String name;

    private String password;

    private String profileImage;

    @Enumerated(EnumType.STRING)
    private SocialProvider provider;  // KAKAO, GOOGLE

    private String providerId;  // 소셜 로그인 고유 ID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;  // USER, ADMIN

    @Column(length = 30)
    private String nickname;

    @Builder.Default
    private int level = 1;

    private int totalPoints;

    public void updateProfile(String name, String nickname, String profileImage) {
        if (StringUtils.hasText(name)) {
            this.name = name;
        }
        if (StringUtils.hasText(nickname)) {
            this.nickname = nickname;
        }
        if (StringUtils.hasText(profileImage)) {
            this.profileImage = profileImage;
        }
    }

    public boolean isSocialUser() {
        return provider != null;
    }

    // Enum 클래스들
    public enum SocialProvider {
        KAKAO, GOOGLE
    }

    public enum Role {
        USER, ADMIN
    }
}
