package com.example.flab.soft.shoppingmallfashion.sms;

import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sms")
public class SMSController {
    private final SMSService smsService;
    @PostMapping("/verification-code")
    public SuccessResult<Void> createCode(@RequestBody SendCodeRequest sendCodeRequest) {
        smsService.sendOne(sendCodeRequest.getNumber());
        return SuccessResult.<Void>builder().build();
    }

    @PostMapping("/verified-phone-number")
    public SuccessResult<Void> verifyCode(@RequestBody VerifyCodeRequest verifyCodeRequest) {
        smsService.cachePhoneNumberIfVerified(verifyCodeRequest.getNumber(), verifyCodeRequest.getCode());
        return SuccessResult.<Void>builder().build();
    }
}
