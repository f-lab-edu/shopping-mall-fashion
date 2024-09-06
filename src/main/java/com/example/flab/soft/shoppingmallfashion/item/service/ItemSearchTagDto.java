package com.example.flab.soft.shoppingmallfashion.item.service;

import com.example.flab.soft.shoppingmallfashion.item.domain.Item;
import com.example.flab.soft.shoppingmallfashion.item.domain.ItemSearchKeyword;
import com.example.flab.soft.shoppingmallfashion.item.domain.SearchKeyword;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemSearchTagDto {
    private Integer tags_count;
    private List<String> searchTags;

    @Builder
    public ItemSearchTagDto(Item item) {
        this.tags_count = item.getItemSearchKeywords().size();
        this.searchTags = item.getItemSearchKeywords().stream()
                .map(ItemSearchKeyword::getSearchKeyword)
                .map(SearchKeyword::getName)
                .toList();
    }
}
