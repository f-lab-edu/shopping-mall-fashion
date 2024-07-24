package com.example.flab.soft.shoppingmallfashion.item.service;

import com.example.flab.soft.shoppingmallfashion.item.domain.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemBriefDto {
    private String itemName;
    private String storeName;
    private Integer originalPrice;
    private Integer salePrice;
    private Integer reviewGrade;
    private Integer reviewCount;
    private String saleState;

    public ItemBriefDto(Item item) {
        this.itemName = item.getName();
        this.storeName = item.getStore().getName();
        this.originalPrice = item.getOriginalPrice();
        this.salePrice = item.getSalePrice();
        this.reviewGrade = item.getItemStats().getReviewGrade();
        this.reviewCount = item.getItemStats().getReviewCount();
        this.saleState = item.getSaleState().name();
    }
}
