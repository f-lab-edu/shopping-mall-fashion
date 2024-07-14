package com.example.flab.soft.shoppingmallfashion.store.controller;

import com.example.flab.soft.shoppingmallfashion.store.service.StoreUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreFieldUpdateRequest {
    private String name;
    private String logo;
    private String description;

    public StoreUpdateDto buildStoreUpdateDto() {
        return StoreUpdateDto.builder()
                .name(name)
                .logo(logo)
                .description(description)
                .build();
    }
}
