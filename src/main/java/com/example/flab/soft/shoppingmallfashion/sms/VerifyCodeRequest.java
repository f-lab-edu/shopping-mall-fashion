package com.example.flab.soft.shoppingmallfashion.sms;

import lombok.Getter;

@Getter
public class VerifyCodeRequest {
    private String number;
    private String code;
}
