package com.example.flab.soft.shoppingmallfashion.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResult {
    private String code;
    private String message;

    @Builder
    public ErrorResult(ErrorEnum errorEnum) {
        code = errorEnum.getCode();
        message = errorEnum.getMessage();
    }
}
