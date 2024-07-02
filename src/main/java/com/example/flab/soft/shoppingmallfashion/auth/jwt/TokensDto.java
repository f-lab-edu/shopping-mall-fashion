package com.example.flab.soft.shoppingmallfashion.auth.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokensDto {
    private String accessToken;
    private String refreshToken;
}
