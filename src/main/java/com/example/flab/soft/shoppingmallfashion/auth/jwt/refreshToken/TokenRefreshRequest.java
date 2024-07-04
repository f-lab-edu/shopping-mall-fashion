package com.example.flab.soft.shoppingmallfashion.auth.jwt.refreshToken;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenRefreshRequest {
    @NotBlank
    private String refreshToken;

    @Builder
    public TokenRefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
