package com.example.flab.soft.shoppingmallfashion.exception;

import java.util.EnumSet;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final EnumSet<ErrorEnum> errorEnumSet = EnumSet.allOf(ErrorEnum.class);
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResult> exceptionHandler(ApiException e) {
        ErrorEnum error = e.getError();

        if (errorEnumSet.contains(error)) {
            return toErrorResponseEntity(error);
        } else return toErrorResponseEntity(ErrorEnum.FAILED_INTERNAL_SYSTEM_PROCESSING);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResult> exceptionHandler(MethodArgumentNotValidException e) {
        return toErrorResponseEntity(ErrorEnum.INVALID_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResult> exceptionHandler(Exception e) {
        return toErrorResponseEntity(ErrorEnum.FAILED_INTERNAL_SYSTEM_PROCESSING);
    }

    private ResponseEntity<ErrorResult> toErrorResponseEntity(ErrorEnum error) {
        return ResponseEntity
                .status(error.getStatus())
                .body(new ErrorResult(error));
    }
}
