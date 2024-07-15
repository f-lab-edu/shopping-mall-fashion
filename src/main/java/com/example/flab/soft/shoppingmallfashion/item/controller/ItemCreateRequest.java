package com.example.flab.soft.shoppingmallfashion.item.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCreateRequest {
    @NotBlank
    @Size(max = 30)
    private String name;
    @NotNull
    @Min(value = 0)
    private Integer price;
    @Min(value = 0)
    private Integer discountAppliedPrice;
    @Size(max = 1000)
    private String description;
    @NotNull
    @Size(max = 45)
    private String sex;
    @NotNull
    private Long storeId;
    @NotNull
    private Long categoryId;
}