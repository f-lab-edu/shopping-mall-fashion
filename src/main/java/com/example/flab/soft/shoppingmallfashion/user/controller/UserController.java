package com.example.flab.soft.shoppingmallfashion.user.controller;

import com.example.flab.soft.shoppingmallfashion.auth.AuthUser;
import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import com.example.flab.soft.shoppingmallfashion.user.service.UserDto;
import com.example.flab.soft.shoppingmallfashion.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @PatchMapping("/me/password")
    public SuccessResult<Void> updatePassword(
            @AuthenticationPrincipal AuthUser user,
            @RequestBody @Validated PasswordUpdateRequest passwordUpdateRequest) {
        userService.changePassword(
                user.getId(), passwordUpdateRequest.getCurrentPassword(), passwordUpdateRequest.getNewPassword());
        return SuccessResult.<Void>builder().build();
    }

    @PatchMapping("/me/email")
    public SuccessResult<UserDto> updateEmail(
            @AuthenticationPrincipal AuthUser user,
            @RequestBody @Validated EmailUpdateRequest emailUpdateRequest) {
        UserDto userDto = userService.updateEmail(user.getId(), emailUpdateRequest.getEmail());
        return SuccessResult.<UserDto>builder().response(userDto).build();
    }

    @PatchMapping("/me/realName")
    public SuccessResult<UserDto> updateRealName(
            @AuthenticationPrincipal AuthUser user,
            @RequestBody @Validated RealNameUpdateRequest realNameUpdateRequest) {
        UserDto userDto = userService.updateRealName(user.getId(), realNameUpdateRequest.getRealName());
        return SuccessResult.<UserDto>builder().response(userDto).build();
    }

    @PatchMapping("/me/cellphone")
    public SuccessResult<UserDto> updateCellphone(
            @AuthenticationPrincipal AuthUser user,
            @RequestBody @Validated CellphoneUpdateRequest cellphoneUpdateRequest) {
        UserDto userDto = userService.updateCellphone(user.getId(), cellphoneUpdateRequest.getCellphoneNumber());
        return SuccessResult.<UserDto>builder().response(userDto).build();
    }

    @PatchMapping("/me/nickname")
    public SuccessResult<UserDto> updateNickname(
            @AuthenticationPrincipal AuthUser user,
            @RequestBody @Validated NicknameUpdateRequest nicknameUpdateRequest) {
        UserDto userDto = userService.updateNickname(user.getId(), nicknameUpdateRequest.getNickname());
        return SuccessResult.<UserDto>builder().response(userDto).build();
    }

    @DeleteMapping("/me")
    public SuccessResult<Void> withdraw(@AuthenticationPrincipal AuthUser user) {
        userService.withdraw(user.getId());
        return SuccessResult.<Void>builder().build();
    }
}
