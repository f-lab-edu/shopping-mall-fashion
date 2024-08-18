package com.example.flab.soft.shoppingmallfashion.item.controller;

import com.example.flab.soft.shoppingmallfashion.item.service.ItemBriefDto;
import com.example.flab.soft.shoppingmallfashion.item.service.ItemDetailsDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto {
    private ItemDetailsDto itemDetailsDto;
    private List<ItemBriefDto> sameStoreItems;
    private List<ItemBriefDto> sameCategoryItems;
    private List<ItemBriefDto> relatedItems;
}
