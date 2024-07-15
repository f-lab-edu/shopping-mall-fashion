package com.example.flab.soft.shoppingmallfashion.exception;

import java.util.EnumSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Slf4j
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

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResult> exceptionHandler(NoResourceFoundException e) {
        return toErrorResponseEntity(ErrorEnum.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResult> exceptionHandler(IllegalArgumentException e) {
        return toErrorResponseEntity(ErrorEnum.INVALID_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResult> exceptionHandler(Exception e) {
        log.error("Internal Error = {}", e.getMessage());
        return toErrorResponseEntity(ErrorEnum.FAILED_INTERNAL_SYSTEM_PROCESSING);
    }

    private ResponseEntity<ErrorResult> toErrorResponseEntity(ErrorEnum error) {
        return ResponseEntity
                .status(error.getStatus())
                .body(new ErrorResult(error));
    }
}
