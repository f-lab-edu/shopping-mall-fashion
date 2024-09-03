package com.example.flab.soft.shoppingmallfashion.sms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyCodeByEmailRequest {
    @NotBlank
    private String email;
    @Pattern(regexp = "^[1-9]\\d{3}$")
    private String code;
}