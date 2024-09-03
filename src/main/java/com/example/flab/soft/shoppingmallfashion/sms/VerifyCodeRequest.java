package com.example.flab.soft.shoppingmallfashion.sms;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyCodeRequest {
    @NotBlank
    private String verificationId;
    @NotBlank
    private String code;
}
