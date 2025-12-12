package com.newconomy.member.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthRequestDTO {

    @Getter
    @NoArgsConstructor
    public static class SignUpRequestDTO {
        private String email;
        private String password;
        private String name;
        private String nickname;
    }

    @Getter
    @NoArgsConstructor
    public static class LoginRequestDTO {
        private String email;
        private String password;
    }
}
