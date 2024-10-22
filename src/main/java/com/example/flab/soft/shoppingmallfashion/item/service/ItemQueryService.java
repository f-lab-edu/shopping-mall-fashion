package com.example.flab.soft.shoppingmallfashion.item.service;

import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.item.domain.Item;
import com.example.flab.soft.shoppingmallfashion.item.domain.ItemOption;
import com.example.flab.soft.shoppingmallfashion.item.domain.ItemRelation;
import com.example.flab.soft.shoppingmallfashion.item.domain.Sex;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemOptionRepository;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemRelationRepository;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemQueryService {
    private final ItemRepository itemRepository;
    private final ItemOptionRepository itemOptionRepository;
    private final ItemRelationRepository itemRelationRepository;
    private final ItemCacheService itemCacheService;

    @Transactional(readOnly = true)
    public Page<ItemBriefDto> getItems(Integer minPrice, Integer maxPrice,
                                       Long categoryId, Long storeId, Sex sex, Pageable pageable) {
        return new PageImpl<>(
                itemCacheService.getItems(minPrice, maxPrice, categoryId, storeId, sex, pageable).getItems(),
                pageable, 10);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "STOCKS",
            key = "#itemOptionId",
            cacheManager = "cacheManager")
    public ItemOptionStocksDto getStocks(Long itemOptionId) {
        ItemOption itemOption = itemOptionRepository.findById(itemOptionId)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));
        return ItemOptionStocksDto.builder()
                .id(itemOption.getId())
                .stocks(itemOption.getStocksCount())
                .build();
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "ITEM_LIST_COUNT",
            key = "#categoryId + ':' + #storeId",
            cacheManager = "cacheManager")
    public ItemsCountDto getItemCounts(Integer minPrice, Integer maxPrice,
                                       Long categoryId, Long storeId, Sex sex) {
        return ItemsCountDto.builder()
                .count(itemRepository.countByFilters(minPrice, maxPrice, categoryId, storeId, sex))
                .build();
    }

    @Transactional(readOnly = true)
    public Page<ItemBriefDto> getItemsWithKeyword(String keyword, Pageable pageable) {
        return itemRepository.findAllWithKeyword(keyword, pageable)
                .map(ItemBriefDto::new);
    }

    @Cacheable(cacheNames = "ITEM_DETAILS",
            key = "#itemId",
            cacheManager = "cacheManager")
    @Transactional(readOnly = true)
    public ItemDetailsDto getItemDetails(Long itemId) {
        Item item = itemRepository.findItemJoinFetchById(itemId)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));
        return ItemDetailsDto.builder().item(item).build();
    }

    @Cacheable(cacheNames = "TOP_ITEMS_STORE",
            key = "#storeId",
            cacheManager = "cacheManager")
    @Transactional(readOnly = true)
    public ItemBriefDtos getTopItemsByStore(Long storeId) {
        Pageable pageable = PageRequest.of(0, 20);
        return ItemBriefDtos.builder()
                .items(itemRepository.findTopItemsByStoreId(storeId, pageable)
                        .stream()
                        .map(ItemBriefDto::new)
                        .toList())
                .build();
    }

    @Cacheable(cacheNames = "TOP_ITEMS_CATEGORY",
            key = "'top-items:category:' + #categoryId",
            cacheManager = "cacheManager")
    @Transactional(readOnly = true)
    public ItemBriefDtos getTopItemsByCategory(Long categoryId) {
        Pageable pageable = PageRequest.of(0, 20);
        return ItemBriefDtos.builder()
                .items(itemRepository.findTopItemsByCategoryId(categoryId, pageable)
                        .stream()
                        .map(ItemBriefDto::new)
                        .toList())
                .build();
    }

    @Transactional(readOnly = true)
    public List<ItemBriefDto> getRelatedItems(Long itemId) {
        Pageable pageable = PageRequest.of(0, 20);
        return itemRelationRepository.findTopRelatedById(itemId, pageable)
                .stream()
                .map(ItemRelation::getRelatedItem)
                .map(ItemBriefDto::new)
                .toList();
    }
}
