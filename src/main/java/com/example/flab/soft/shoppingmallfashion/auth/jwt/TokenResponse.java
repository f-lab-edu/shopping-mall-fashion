package com.example.flab.soft.shoppingmallfashion.auth.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private String type = "Bearer";

    @Builder
    public TokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
