package com.example.flab.soft.shoppingmallfashion.exception;

import lombok.Getter;

@Getter
public class ErrorResult {
    private final String code;
    private final String message;

    public ErrorResult(ErrorEnum errorEnum) {
        code = errorEnum.getCode();
        message = errorEnum.getMessage();
    }
}
