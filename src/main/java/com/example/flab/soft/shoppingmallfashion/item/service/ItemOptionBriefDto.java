package com.example.flab.soft.shoppingmallfashion.item.service;

import com.example.flab.soft.shoppingmallfashion.item.domain.ItemOption;
import com.example.flab.soft.shoppingmallfashion.item.domain.SaleState;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemOptionBriefDto {
    private Long itemOptionId;
    private String name;
    private String option;
    private SaleState saleState;

    @Builder
    public ItemOptionBriefDto(ItemOption itemOption) {
        this.itemOptionId = itemOption.getId();
        this.name = itemOption.getName();
        this.option = itemOption.getOptionValue();
        this.saleState = itemOption.getSaleState();
    }
}
