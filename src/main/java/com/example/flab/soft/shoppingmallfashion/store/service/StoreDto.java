package com.example.flab.soft.shoppingmallfashion.store.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreDto {
    private Long id;
    private String name;
    private String logo;
    private String description;
    private String businessRegistrationNumber;
    private String saleState;
}
