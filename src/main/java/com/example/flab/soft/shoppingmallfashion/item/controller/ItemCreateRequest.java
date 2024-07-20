package com.example.flab.soft.shoppingmallfashion.item.controller;

import com.example.flab.soft.shoppingmallfashion.item.domain.SaleState;
import com.example.flab.soft.shoppingmallfashion.item.domain.Sex;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
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
    private Integer originalPrice;
    @Min(value = 0)
    private Integer salePrice;
    @Size(max = 1000)
    private String description;
    private Sex sex;
    private SaleState saleState;
    @NotNull
    private Long storeId;
    @NotNull
    private List<ProductDto> products;
    @NotNull
    private Long categoryId;
}