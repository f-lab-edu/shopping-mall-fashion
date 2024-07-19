package com.example.flab.soft.shoppingmallfashion.util;

import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import java.util.Objects;

public class NotNullValidator {
    public static<T> T requireNotNull(T value) {
        try {
            return Objects.requireNonNull(value);
        } catch (NullPointerException e) {
            throw new ApiException(ErrorEnum.INVALID_REQUEST);
        }
    }
}
