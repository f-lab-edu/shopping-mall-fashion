package com.example.flab.soft.shoppingmallfashion.user.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CellphoneUpdateRequest {
    @NotBlank
    private String cellphoneNumber;

    @Builder
    public CellphoneUpdateRequest(String cellphoneNumber) {
        this.cellphoneNumber = cellphoneNumber;
    }
}
