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
    INACTIVATED_USER(HttpStatus.UNAUTHORIZED, "비활성화된 계정입니다."),
    USERNAME_DUPLICATED(HttpStatus.CONFLICT, "이미 등록된 아이디입니다."),
    EMAIL_DUPLICATED(HttpStatus.CONFLICT, "이미 등록된 이메일입니다."),
    CELLPHONE_DUPLICATED(HttpStatus.CONFLICT, "이미 등록된 전화번호입니다."),
    NICKNAME_DUPLICATED(HttpStatus.CONFLICT, "이미 등록된 닉네임입니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    // Authentication Exception
    AUTHENTICATION_FAILED(HttpStatus.BAD_REQUEST, "아이디 또는 비밀번호가 잘못되었습니다."),
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "인증되지 않은 접근입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),
    TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "유효 기간이 만료된 토큰입니다."),
    // Address
    NO_SUCH_ADDRESS(HttpStatus.BAD_REQUEST, "존재하지 않는 주소입니다."),
    FORBIDDEN_ADDRESS_REQUEST(HttpStatus.FORBIDDEN, "허용되지 않는 주소 요청입니다."),
    // Store
    STORE_NAME_DUPLICATED(HttpStatus.CONFLICT, "이미 등록된 브랜드입니다."),
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 브랜드가 존재하지 않습니다."),
    // Item
    OUT_OF_STOCK(HttpStatus.CONFLICT, "재고가 남아 있지 않습니다."),
    ALREADY_SOLD_OUT(HttpStatus.CONFLICT, "이미 품절된 상품입니다."),
    ALREADY_ON_SALE(HttpStatus.CONFLICT, "이미 판매가 시작된 상품입니다."),
    ALREADY_END_OF_PRODUCTION(HttpStatus.CONFLICT, "이미 단종 처리가 된 상품입니다."),
    // Order
    ALREADY_ON_DELIVERY(HttpStatus.CONFLICT, "이미 배송이 시작된 상품입니다."),
    ALREADY_PAID(HttpStatus.CONFLICT, "이미 결제가 된 상품입니다."),
    NEED_PAYMENT(HttpStatus.CONFLICT, "사용자의 결제가 필요합니다."),
    OUT_OF_COUPON(HttpStatus.CONFLICT, "쿠폰이 모두 소진되었습니다."),
    ALREADY_OWNED_COUPON(HttpStatus.CONFLICT, "다른 사용자가 보유중인 쿠폰입니다."),
    RETRY_GET_COUPON(HttpStatus.CONFLICT, "현재 접속자가 많아 쿠폰 획득에 실패했습니다. 다시 시도해주세요"),
    //Crew
    INVALID_CREW_ID(HttpStatus.BAD_REQUEST, "직원 정보가 올바르지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorEnum(HttpStatus status, String message) {
        this.status = status;
        this.code = name();
        this.message = message;
    }
}
