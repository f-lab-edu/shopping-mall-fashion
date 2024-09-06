package com.example.flab.soft.shoppingmallfashion.user.controller;

import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetails.AuthUser;
import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.sms.VerificationService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
@Slf4j
public class UserController {
    private final UserService userService;
    private final VerificationService verificationService;

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

    @GetMapping("/me/email")
    public SuccessResult<UserEmailDto> findEmail(@AuthenticationPrincipal AuthUser user) {
        return SuccessResult.<UserEmailDto>builder()
                .response(UserEmailDto.builder()
                        .email(user.getEmail())
                        .build())
                .build();
    }

    @PatchMapping("/me/password")
    public SuccessResult<Void> updatePassword(
            @AuthenticationPrincipal AuthUser user,
            @RequestBody @Validated PasswordUpdateRequest passwordUpdateRequest) {
        userService.changePassword(
                user.getId(), passwordUpdateRequest.getCurrentPassword(), passwordUpdateRequest.getNewPassword());
        return SuccessResult.<Void>builder().build();
    }

    @PatchMapping("/me")
    public SuccessResult<UserDto> updateField(
            @AuthenticationPrincipal AuthUser user,
            @RequestBody @Validated FieldUpdateRequest fieldUpdateRequest,
            @RequestParam String type) {
        UserDto userDto = userService.updateField(user.getId(), type, fieldUpdateRequest.getValue());
        return SuccessResult.<UserDto>builder().response(userDto).build();
    }

    @DeleteMapping("/me")
    public SuccessResult<Void> withdraw(@AuthenticationPrincipal AuthUser user) {
        userService.withdraw(user.getId());
        return SuccessResult.<Void>builder().build();
    }

    @PatchMapping("/me/verification/email")
    public SuccessResult<UserDto> verifyEmail(
            @AuthenticationPrincipal AuthUser user,
            @RequestBody @Validated VerifyEmailRequest verifyEmailRequest) {
        if (!user.getEmail().equals(verifyEmailRequest.getEmail())) {
            throw new ApiException(ErrorEnum.FORBIDDEN);
        }
        verificationService.verifyEmail(verifyEmailRequest.getEmail());
        UserDto userDto = userService.setEmailVerified(user.getId());
        return SuccessResult.<UserDto>builder().response(userDto).build();
    }

    @PatchMapping("/me/verification/phone-number")
    public SuccessResult<UserDto> verifyPhoneNumber(
            @AuthenticationPrincipal AuthUser user,
            @RequestBody @Validated VerifyPhoneNumberRequest verifyPhoneNumberRequest) {
        if (!userService.doesPhoneNumberMatchUser(verifyPhoneNumberRequest.getPhoneNumber(), user.getId())) {
            throw new ApiException(ErrorEnum.FORBIDDEN);
        }
        verificationService.verifyPhoneNumber(verifyPhoneNumberRequest.getPhoneNumber());
        UserDto userDto = userService.setPhoneNumberVerified(user.getId());
        return SuccessResult.<UserDto>builder().response(userDto).build();
    }
}
