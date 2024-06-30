package com.example.flab.soft.shoppingmallfashion.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorEnum {
    // System Exception
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    FORBIDDEN_REQUEST(HttpStatus.UNAUTHORIZED, "허용되지 않은 요청입니다."),
    FAILED_INTERNAL_SYSTEM_PROCESSING(HttpStatus.INTERNAL_SERVER_ERROR, "내부 시스템 처리 작업이 실패했습니다. 잠시 후 다시 시도해주세요."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 정보 입니다."),
    // User Exception
    USERNAME_DUPLICATED(HttpStatus.CONFLICT, "이미 등록된 아이디입니다."),
    EMAIL_DUPLICATED(HttpStatus.CONFLICT, "이미 등록된 이메일입니다."),
    CELLPHONE_DUPLICATED(HttpStatus.CONFLICT, "이미 등록된 전화번호입니다."),
    NICKNAME_DUPLICATED(HttpStatus.CONFLICT, "이미 등록된 닉네임입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorEnum(HttpStatus status, String message) {
        this.status = status;
        this.code = name();
        this.message = message;
    }
}
