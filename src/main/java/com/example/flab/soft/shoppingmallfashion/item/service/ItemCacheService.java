package com.example.flab.soft.shoppingmallfashion.item.service;

import com.example.flab.soft.shoppingmallfashion.item.domain.Sex;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemCacheService {
    private final ItemRepository itemRepository;

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "ITEM_LIST",
            key = "#categoryId + ':' + #storeId + ':' + #pageable.pageNumber",
            condition = "#pageable.pageNumber <= 3",
            cacheManager = "cacheManager")
    public ItemsDto getItems(Integer minPrice, Integer maxPrice,
                                       Long categoryId, Long storeId, Sex sex, Pageable pageable) {
        return ItemsDto.builder()
                .items(itemRepository.findAllByFilters(minPrice, maxPrice, categoryId, storeId, sex, pageable)
                        .stream()
                        .map(ItemBriefDto::new)
                        .toList())
                .build();
    }
}
