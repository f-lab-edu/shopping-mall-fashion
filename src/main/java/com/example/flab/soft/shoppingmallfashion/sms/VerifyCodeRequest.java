package com.example.flab.soft.shoppingmallfashion.sms;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyCodeRequest {
    @Pattern(regexp = "^(010[0-9]{8}|[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$")
    private String verificationId;
    @Pattern(regexp = "^[1-9]\\d{3}$")
    private String code;
}
