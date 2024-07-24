package com.example.flab.soft.shoppingmallfashion.item.service;

import com.example.flab.soft.shoppingmallfashion.item.domain.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemBriefDto {
    private Long itemId;
    private Long storeId;
    private Long categoryId;
    private Long largeCategoryId;
    private String itemName;
    private String storeName;
    private String categoryName;
    private String largeCategoryName;
    private Integer originalPrice;
    private Integer salePrice;
    private Integer reviewGrade;
    private Integer reviewCount;
    private String saleState;

    @Builder
    public ItemBriefDto(Item item) {
        this.itemId = item.getId();
        this.storeId = item.getStore().getId();
        this.categoryId = item.getCategory().getId();
        this.largeCategoryId = item.getCategory().getLargeCategory().getId();
        this.itemName = item.getName();
        this.storeName = item.getStore().getName();
        this.categoryName = item.getCategory().getName();
        this.largeCategoryName = item.getCategory().getLargeCategory().getName();
        this.originalPrice = item.getOriginalPrice();
        this.salePrice = item.getSalePrice();
        this.reviewGrade = item.getItemStats().getReviewGrade();
        this.reviewCount = item.getItemStats().getReviewCount();
        this.saleState = item.getSaleState().name();
    }
}
