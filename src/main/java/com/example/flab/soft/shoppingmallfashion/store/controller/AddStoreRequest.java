package com.example.flab.soft.shoppingmallfashion.store.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddStoreRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String logo;
    private String description;
    @Pattern(regexp = "\\d{10}")
    private String businessRegistrationNumber;
}
