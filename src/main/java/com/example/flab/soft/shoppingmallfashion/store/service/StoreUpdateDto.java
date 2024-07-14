package com.example.flab.soft.shoppingmallfashion.store.service;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StoreUpdateDto {
    private String name;
    private String logo;
    private String description;
}
