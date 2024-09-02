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
@RequestMapping("/api/v1/sms")
public class VerificationController {
    private final VerificationService verificationService;
    @PostMapping("/verification-code")
    public SuccessResult<Void> createCode(
            @Validated @RequestBody SendCodeRequest sendCodeRequest) {
        verificationService.sendOne(sendCodeRequest.getPhoneNumber());
        return SuccessResult.<Void>builder().build();
    }

    @PostMapping("/verified-phone-number")
    public SuccessResult<Void> verifyCode(
            @Validated @RequestBody VerifyCodeRequest verifyCodeRequest) {
        verificationService.cachePhoneNumberIfVerified(
                verifyCodeRequest.getPhoneNumber(), verifyCodeRequest.getCode());
        return SuccessResult.<Void>builder().build();
    }
}
