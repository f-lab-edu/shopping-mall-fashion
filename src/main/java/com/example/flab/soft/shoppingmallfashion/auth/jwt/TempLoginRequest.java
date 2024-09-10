package com.example.flab.soft.shoppingmallfashion.auth.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TempLoginRequest {
    private String phoneNumber;
}
