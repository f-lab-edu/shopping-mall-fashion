package com.example.flab.soft.shoppingmallfashion.item.service;

import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.item.domain.Item;
import com.example.flab.soft.shoppingmallfashion.item.domain.ItemSearchKeyword;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemRepository;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemSearchKeywordRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemSearchKeywordService {
    private final ItemRepository itemRepository;
    private final ItemSearchKeywordRepository itemSearchKeywordRepository;

    @Transactional
    public ItemSearchKeywordDto initDefaultSearchKeywords(Long itemId) {
        Item item = findItem(itemId);
        item.selectDefaultKeywords().stream()
                .map(searchKeyword -> saveAsItemSearchKeyword(itemId, searchKeyword, true))
                .forEach(item::addItemSearchKeyword);
        return ItemSearchKeywordDto.builder().item(item).build();
    }

    private Item findItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));
    }

    @Transactional
    public ItemSearchKeywordDto getAllSearchKeywords(Long itemId) {
        return ItemSearchKeywordDto.builder().item(findItem(itemId)).build();
    }

    @Transactional
    public ItemSearchKeywordDto updateItemSearchKeyword(Long itemId, List<String> newSearchKeywords) {
        Item item = findItem(itemId);
        List<String> defaultSearchKeywords = item.getItemSearchKeywords().stream()
                .filter(ItemSearchKeyword::isDefault)
                .map(ItemSearchKeyword::getSearchKeyword)
                .toList();

        item.getItemSearchKeywords().stream()
                .filter(ItemSearchKeyword::isDeletable)
                .filter(itemSearchKeyword -> !newSearchKeywords.contains(itemSearchKeyword.getSearchKeyword()))
                .forEach(itemSearchKeywordRepository::delete);

        newSearchKeywords.stream()
                .distinct()
                .filter(keyword -> !defaultSearchKeywords.contains(keyword))
                .map(searchKeyword -> saveAsItemSearchKeyword(itemId, searchKeyword, false))
                .forEach(item::addItemSearchKeyword);
        return ItemSearchKeywordDto.builder().item(item).build();
    }

    private ItemSearchKeyword saveAsItemSearchKeyword(Long itemId, String searchKeyword, Boolean isDefault) {
        return itemSearchKeywordRepository.findByItemIdAndSearchKeyword(itemId, searchKeyword)
                .orElseGet(() -> itemSearchKeywordRepository.save(ItemSearchKeyword.builder()
                        .itemId(itemId)
                        .searchKeyword(searchKeyword)
                        .isDefault(isDefault)
                        .build()));
    }
}
