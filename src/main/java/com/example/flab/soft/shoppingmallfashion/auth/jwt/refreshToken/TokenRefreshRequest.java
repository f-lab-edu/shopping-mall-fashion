package com.example.flab.soft.shoppingmallfashion.auth.jwt.refreshToken;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class TokenRefreshRequest {
    @Pattern(regexp = "^Bearer .+$", message = "인증 토큰은 'Bearer '로 시작해야 합니다.")
    private String refreshToken;
}
