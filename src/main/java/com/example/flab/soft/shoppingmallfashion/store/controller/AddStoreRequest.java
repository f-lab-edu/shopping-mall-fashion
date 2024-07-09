package com.example.flab.soft.shoppingmallfashion.store.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddStoreRequest {
    private String name;
    private String logo;
    private String description;
    private String businessRegistrationNumber;

    @Builder
    public AddStoreRequest(String name, String logo, String description, String businessRegistrationNumber) {
        this.name = name;
        this.logo = logo;
        this.description = description;
        this.businessRegistrationNumber = businessRegistrationNumber;
    }
}
