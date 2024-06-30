package com.example.flab.soft.shoppingmallfashion.common;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class SuccessResult<T> {
    private T response;
}
