package com.example.flab.soft.shoppingmallfashion.auth.jwt.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NewTokensDto {
    private String accessToken;
    private String refreshToken;
    private final String type = "Bearer";
}
