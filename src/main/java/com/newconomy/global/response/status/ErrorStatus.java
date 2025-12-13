package com.newconomy.global.response.status;

import com.newconomy.global.response.BaseErrorCode;
import com.newconomy.global.response.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER4000", "존재하지 않는 유저입니다."),
    DUPLICATE_MEMBER(HttpStatus.BAD_REQUEST, "MEMBER4001", "이미 존재하는 유저입니다."),
    SOCIAL_LOGIN_MEMBER(HttpStatus.BAD_REQUEST, "MEMBER4002", "소셜 로그인 계정입니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "MEMBER4003", "비밀번호가 일치하지 않습니다."),

    NEWS_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "NEWS_CATEGORY4000", "존재하지 않는 뉴스 카테고리입니다.")
    ;

    private final HttpStatus httpStatus;
    private String code;
    private String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
