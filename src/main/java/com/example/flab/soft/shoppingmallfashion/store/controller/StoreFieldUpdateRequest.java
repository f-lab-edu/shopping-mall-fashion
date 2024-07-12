package com.example.flab.soft.shoppingmallfashion.store.controller;

import com.example.flab.soft.shoppingmallfashion.common.validator.NullableNotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreFieldUpdateRequest {
    @NullableNotBlank
    private String name;
    @NullableNotBlank
    private String logo;
    @NullableNotBlank
    private String description;
}
