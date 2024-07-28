package com.example.flab.soft.shoppingmallfashion.item.controller;

import com.example.flab.soft.shoppingmallfashion.item.domain.Sex;
import lombok.Data;

@Data
public class ItemListRequest {
    private Integer minPrice;
    private Integer maxPrice;
    private Long categoryId;
    private Long storeId;
    private Sex sex;
}
