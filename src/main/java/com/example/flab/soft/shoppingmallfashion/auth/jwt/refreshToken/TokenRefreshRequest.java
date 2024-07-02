package com.example.flab.soft.shoppingmallfashion.auth.jwt.refreshToken;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TokenRefreshRequest {
    @NotBlank
    private String refreshToken;
}
