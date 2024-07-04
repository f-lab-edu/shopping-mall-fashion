package com.example.flab.soft.shoppingmallfashion.auth.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequest {
    private String username;
    private String password;
}
