package com.example.flab.soft.shoppingmallfashion.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final ErrorEnum error;

    public ApiException(ErrorEnum e) {
        super(e.getMessage());
        this.error = e;
    }
}
