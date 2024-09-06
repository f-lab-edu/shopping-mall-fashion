package com.example.flab.soft.shoppingmallfashion.item.service;

import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.item.domain.Item;
import com.example.flab.soft.shoppingmallfashion.item.domain.ItemSearchKeyword;
import com.example.flab.soft.shoppingmallfashion.item.domain.SearchKeyword;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemRepository;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemSearchKeywordRepository;
import com.example.flab.soft.shoppingmallfashion.item.repository.SearchKeywordRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemSearchKeywordService {
    private final ItemRepository itemRepository;
    private final SearchKeywordRepository searchKeywordRepository;
    private final ItemSearchKeywordRepository itemSearchKeywordRepository;

    @Transactional
    public ItemSearchKeywordDto initDefaultSearchKeywords(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));

        item.selectDefaultKeywords().stream()
                .map(searchKeyword -> saveAsItemSearchKeyword(item, searchKeyword, true))
                .forEach(item::addItemSearchKeyword);
        return ItemSearchKeywordDto.builder().item(item).build();
    }

    @Transactional
    public ItemSearchKeywordDto getAllSearchKeywords(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));
        return ItemSearchKeywordDto.builder().item(item).build();
    }

    @Transactional
    public ItemSearchKeywordDto updateItemSearchKeyword(Long itemId, List<String> newSearchKeywords) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));

        List<String> defaultSearchKeywords = item.getItemSearchKeywords().stream()
                .filter(ItemSearchKeyword::isDefault)
                .map(ItemSearchKeyword::getKeyword)
                .toList();

        item.getItemSearchKeywords().stream()
                .filter(ItemSearchKeyword::isDeletable)
                .filter(itemSearchKeyword -> !newSearchKeywords.contains(itemSearchKeyword.getKeyword()))
                .forEach(itemSearchKeywordRepository::delete);

        newSearchKeywords.stream()
                .distinct()
                .filter(keyword -> !defaultSearchKeywords.contains(keyword))
                .map(searchKeyword -> saveAsItemSearchKeyword(item, searchKeyword, false))
                .forEach(item::addItemSearchKeyword);
        return ItemSearchKeywordDto.builder().item(item).build();
    }

    private ItemSearchKeyword saveAsItemSearchKeyword(Item item, String searchKeyword, Boolean isDefault) {
        SearchKeyword searchKeywordEntity = searchKeywordRepository.findByName(searchKeyword)
                .orElseGet(() -> searchKeywordRepository.save(SearchKeyword.builder()
                        .name(searchKeyword)
                        .build()));
        return itemSearchKeywordRepository.findByItemIdAndSearchKeyword(item.getId(), searchKeywordEntity)
                .orElseGet(() -> itemSearchKeywordRepository.save(ItemSearchKeyword.builder()
                        .itemId(item.getId())
                        .searchKeyword(searchKeywordEntity)
                        .isDefault(isDefault)
                        .build()));
    }
}
