package com.example.flab.soft.shoppingmallfashion.sms;

import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/verification")
public class VerificationController {
    private final VerificationService verificationService;

    @PostMapping("/sms/verification-code")
    public SuccessResult<Void> createCodeBySms(
            @Validated @RequestBody SendCodeRequest sendCodeRequest) {
        verificationService.sendMessage(sendCodeRequest.getVerificationId());
        return SuccessResult.<Void>builder().build();
    }

    @PostMapping("/email/verification-code")
    public SuccessResult<Void> createCodeByEmail(
            @Validated @RequestBody SendCodeRequest sendCodeRequest) {
        verificationService.sendEmail(sendCodeRequest.getVerificationId());
        return SuccessResult.<Void>builder().build();
    }

    @PostMapping("/verified-phone-number")
    public SuccessResult<Void> verifyPhoneNumber(
            @Validated @RequestBody VerifyPhoneNumberRequest verifyPhoneNumberRequest) {
        verificationService.verifyCodeWithPhoneNumber(
                verifyPhoneNumberRequest.getPhoneNumber(), verifyPhoneNumberRequest.getCode());
        return SuccessResult.<Void>builder().build();
    }

    @PostMapping("/verified-email")
    public SuccessResult<Void> verifyEmail(
            @Validated @RequestBody VerifyEmailRequest verifyEmailRequest) {
        verificationService.verifyCodeWithEamil(
                verifyEmailRequest.getEmail(), verifyEmailRequest.getCode());
        return SuccessResult.<Void>builder().build();
    }
}
