package com.example.flab.soft.shoppingmallfashion.common;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SuccessResult {
    private final String code = "SUCCESS";
    private final String message = "";
    private Object response;

    public SuccessResult(Object response) {
        this.response = response;
    }
}
