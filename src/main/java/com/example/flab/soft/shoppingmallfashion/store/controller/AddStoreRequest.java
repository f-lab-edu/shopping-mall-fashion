package com.example.flab.soft.shoppingmallfashion.store.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddStoreRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String logo;
    private String description;
    @Pattern(regexp = "\\d{10}")
    private String businessRegistrationNumber;

    @Builder
    public AddStoreRequest(String name, String logo, String description, String businessRegistrationNumber) {
        this.name = name;
        this.logo = logo;
        this.description = description;
        this.businessRegistrationNumber = businessRegistrationNumber;
    }
}
