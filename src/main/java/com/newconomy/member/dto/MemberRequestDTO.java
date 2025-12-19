package com.newconomy.member.dto;

import lombok.Getter;

public class MemberRequestDTO {

    @Getter
    public static class ProfileChangeRequestDTO {
        private String name;
        private String nickName;
        private String profileImage;
    }
}
