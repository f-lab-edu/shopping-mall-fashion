package com.example.flab.soft.shoppingmallfashion.item.service;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ItemsDto {
    private List<ItemBriefDto> items;
}
