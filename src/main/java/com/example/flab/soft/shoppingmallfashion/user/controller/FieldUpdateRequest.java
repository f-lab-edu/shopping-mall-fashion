package com.example.flab.soft.shoppingmallfashion.user.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FieldUpdateRequest {
    @NotBlank
    private String value;

    @Builder
    public FieldUpdateRequest(String nickname) {
        this.value = nickname;
    }
}
