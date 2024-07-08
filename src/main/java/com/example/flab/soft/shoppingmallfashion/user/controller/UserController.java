package com.example.flab.soft.shoppingmallfashion.user.controller;

import com.example.flab.soft.shoppingmallfashion.auth.AuthUser;
import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import com.example.flab.soft.shoppingmallfashion.user.service.UserDto;
import com.example.flab.soft.shoppingmallfashion.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public SuccessResult<Void> signup(@RequestBody @Validated UserSignUpRequest userSignUpRequest) {
        userService.signUp(userSignUpRequest);
        return SuccessResult.<Void>builder().build();
    }

    @GetMapping("/me")
    public SuccessResult<UserDto> me(@AuthenticationPrincipal AuthUser user) {
        UserDto userDto = userService.findUser(user.getId());
        return SuccessResult.<UserDto>builder().response(userDto).build();
    }
}
