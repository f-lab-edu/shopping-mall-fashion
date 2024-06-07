package com.example.flab.soft.shoppingmallfashion.user.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserSigninInfo {
    private String signinId;
    private String password;

    public UserSigninInfo(String signinId, String password) {
        this.signinId = signinId;
        this.password = password;
    }
}
