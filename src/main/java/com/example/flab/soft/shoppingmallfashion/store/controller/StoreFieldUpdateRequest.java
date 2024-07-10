package com.example.flab.soft.shoppingmallfashion.store.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoreFieldUpdateRequest {
    @NotBlank
    private String value;

    @Builder
    public StoreFieldUpdateRequest(String value) {
        this.value = value;
    }
}
