package com.example.flab.soft.shoppingmallfashion.user.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RealNameUpdateRequest {
    @NotBlank
    private String realName;

    @Builder
    public RealNameUpdateRequest(String realName) {
        this.realName = realName;
    }
}
