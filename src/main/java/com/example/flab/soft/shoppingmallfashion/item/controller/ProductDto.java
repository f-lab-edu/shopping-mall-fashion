package com.example.flab.soft.shoppingmallfashion.item.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    @NotBlank
    private String name;
    @NotBlank
    private String size;
    private String option;
    @NotBlank
    private String saleState;
}
