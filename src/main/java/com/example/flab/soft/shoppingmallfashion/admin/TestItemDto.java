package com.example.flab.soft.shoppingmallfashion.admin;

import com.example.flab.soft.shoppingmallfashion.item.controller.ItemOptionDto;
import com.example.flab.soft.shoppingmallfashion.item.domain.SaleState;
import com.example.flab.soft.shoppingmallfashion.item.domain.Sex;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestItemDto {
    private String name;
    private Integer originalPrice;
    private Integer salePrice;
    private String description;
    private Sex sex;
    private SaleState saleState;
    private Long storeId;
    private List<ItemOptionDto> itemOptions;
    private Long categoryId;
    private Long isModifiedBy;
    private int orderCount;
}