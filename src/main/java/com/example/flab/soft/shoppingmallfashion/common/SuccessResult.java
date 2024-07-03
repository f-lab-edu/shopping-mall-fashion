package com.example.flab.soft.shoppingmallfashion.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SuccessResult<T> {
    private T response;
}
