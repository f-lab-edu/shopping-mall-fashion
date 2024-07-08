package com.example.flab.soft.shoppingmallfashion.user.controller;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailUpdateRequest {
    @Email
    private String email;

    @Builder
    public EmailUpdateRequest(String email) {
        this.email = email;
    }
}
