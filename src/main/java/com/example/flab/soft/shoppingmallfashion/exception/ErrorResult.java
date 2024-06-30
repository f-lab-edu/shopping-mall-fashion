package com.example.flab.soft.shoppingmallfashion.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResult {
    private final String code;
    private final String message;

    @Builder
    public ErrorResult(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
