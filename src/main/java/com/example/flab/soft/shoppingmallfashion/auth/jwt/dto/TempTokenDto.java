package com.example.flab.soft.shoppingmallfashion.auth.jwt.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TempTokenDto {
    private String accessToken;
    private final String type = "Bearer";
}
