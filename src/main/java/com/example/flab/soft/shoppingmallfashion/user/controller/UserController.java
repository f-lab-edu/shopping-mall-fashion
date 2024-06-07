package com.example.flab.soft.shoppingmallfashion.user.controller;

import com.example.flab.soft.shoppingmallfashion.user.service.UserService;
import com.example.flab.soft.shoppingmallfashion.user.service.exception.badField.SignUpBadFieldException;
import com.example.flab.soft.shoppingmallfashion.user.service.exception.fieldConflict.SignUpFieldConflictException;
import com.example.flab.soft.shoppingmallfashion.user.service.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public void signup(@RequestBody SignUpRequest request) {
        userService.signUp(
                new UserSignUpDto.UserSignUpDtoBuilder()
                        .signinId(request.signinId)
                        .password(request.password)
                        .name(request.name)
                        .email(request.email)
                        .cellphoneNumber(request.cellphoneNumber)
                        .nickname(request.nickname)
                        .build()
                );
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Void> handleException(UserException e) {
        int status;
        if (e instanceof SignUpBadFieldException) {
            status = 400;
        } else if (e instanceof SignUpFieldConflictException) {
            status = 409;
        } else {
            status = 500;
        }
        return ResponseEntity.status(status).build();
    }

    @Getter
    @AllArgsConstructor
    static class SignUpRequest {
        private final String signinId;
        private final String password;
        private final String name;
        private final String email;
        private final String cellphoneNumber;
        private final String nickname;
    }
}
