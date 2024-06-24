package com.example.flab.soft.shoppingmallfashion.user.controller;

import com.example.flab.soft.shoppingmallfashion.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public void signup(@RequestBody @Validated UserSignUpInfo userSignUpInfo) {
        userService.signUp(userSignUpInfo);
    }
}
