package com.example.flab.soft.shoppingmallfashion.item.service;

import com.example.flab.soft.shoppingmallfashion.item.domain.Item;
import com.example.flab.soft.shoppingmallfashion.item.domain.ItemSearchKeyword;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemSearchKeywordDto {
    private Integer keywords_count;
    private List<String> defaultSearchKeywords;
    private List<String> userDesignationSearchKeywords;

    @Builder
    public ItemSearchKeywordDto(Item item) {
        this.keywords_count = item.getItemSearchKeywords().size();
        this.defaultSearchKeywords = item.getItemSearchKeywords().stream()
                .filter(ItemSearchKeyword::isDefault)
                .map(ItemSearchKeyword::getKeyword)
                .toList();
        this.userDesignationSearchKeywords = item.getItemSearchKeywords().stream()
                .filter(itemSearchKeyword -> !itemSearchKeyword.isDefault())
                .map(ItemSearchKeyword::getKeyword)
                .toList();
    }
}
