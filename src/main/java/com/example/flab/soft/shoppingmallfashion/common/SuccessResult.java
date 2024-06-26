package com.example.flab.soft.shoppingmallfashion.common;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SuccessResult<T> {
    private final String code = "SUCCESS";
    private final String message = "";
    private T response;

    public SuccessResult(T response) {
        this.response = response;
    }
}
