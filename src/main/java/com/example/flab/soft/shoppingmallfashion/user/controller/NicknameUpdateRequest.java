package com.example.flab.soft.shoppingmallfashion.user.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NicknameUpdateRequest {
    @NotBlank
    private String nickname;

    @Builder
    public NicknameUpdateRequest(String nickname) {
        this.nickname = nickname;
    }
}
