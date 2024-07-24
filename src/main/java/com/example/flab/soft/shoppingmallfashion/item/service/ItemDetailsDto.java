package com.example.flab.soft.shoppingmallfashion.item.service;

import com.example.flab.soft.shoppingmallfashion.item.domain.Item;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemDetailsDto {
    private ItemBriefDto itemBriefDto;
    private String description;
    private List<ItemOptionBriefDto> itemOptionBriefs;

    @Builder
    public ItemDetailsDto(Item item) {
        this.itemBriefDto = ItemBriefDto.builder().item(item).build();
        this.description = item.getDescription();
        this.itemOptionBriefs = item.getItemOptions().stream()
                .map(ItemOptionBriefDto::new).toList();
    }
}
