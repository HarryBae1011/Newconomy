package com.newconomy.member.auth.dto;

import lombok.*;

public class AuthResponseDTO {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUpResponseDTO {
        private Long memberId;
        private String memberEmail;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResponseDTO {
        private String accessToken;
        private Long memberId;
    }
}
