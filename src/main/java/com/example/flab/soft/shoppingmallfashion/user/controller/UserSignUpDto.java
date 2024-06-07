package com.example.flab.soft.shoppingmallfashion.user.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
public class UserSignUpDto {
    private String signinId;
    private String password;
    private String name;
    private String email;
    private String cellphoneNumber;
    private String nickname;
}
