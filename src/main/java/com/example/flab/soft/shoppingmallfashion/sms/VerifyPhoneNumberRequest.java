package com.example.flab.soft.shoppingmallfashion.sms;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyPhoneNumberRequest {
    @NotBlank
    private String phoneNumber;
    @NotBlank
    private String code;
}
