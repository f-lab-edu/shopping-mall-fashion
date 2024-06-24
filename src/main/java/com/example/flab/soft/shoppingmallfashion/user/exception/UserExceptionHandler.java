package com.example.flab.soft.shoppingmallfashion.user.exception;

import com.example.flab.soft.shoppingmallfashion.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.example.flab.soft.shoppingmallfashion.user.controller")
public class UserExceptionHandler {
    @ExceptionHandler(UserException.class)
    public ResponseEntity<Void> handleException(UserException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
