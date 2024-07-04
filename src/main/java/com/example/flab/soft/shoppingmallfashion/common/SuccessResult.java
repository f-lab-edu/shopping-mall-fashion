package com.example.flab.soft.shoppingmallfashion.common;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SuccessResult<T> {
    private T response;

    @Builder
    public SuccessResult(T response) {
        this.response = response;
    }
}
