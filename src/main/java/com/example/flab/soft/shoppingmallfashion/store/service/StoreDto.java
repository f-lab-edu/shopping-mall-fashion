package com.example.flab.soft.shoppingmallfashion.store.service;

import lombok.Builder;
import lombok.Getter;

@Getter
public class StoreDto {
    private Long id;
    private String name;
    private String logo;
    private String description;
    private String businessRegistrationNumber;

    @Builder
    public StoreDto(Long id, String name, String logo, String description, String businessRegistrationNumber) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.description = description;
        this.businessRegistrationNumber = businessRegistrationNumber;
    }
}
