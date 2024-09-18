package com.example.flab.soft.shoppingmallfashion.item.service;

import com.example.flab.soft.shoppingmallfashion.category.Category;
import com.example.flab.soft.shoppingmallfashion.category.CategoryRepository;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.item.controller.ItemCreateRequest;
import com.example.flab.soft.shoppingmallfashion.item.domain.Item;
import com.example.flab.soft.shoppingmallfashion.item.domain.ItemOption;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemRepository;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemOptionRepository;
import com.example.flab.soft.shoppingmallfashion.store.repository.Store;
import com.example.flab.soft.shoppingmallfashion.store.repository.StoreRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemCommandService {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;
    private final ItemOptionRepository itemOptionRepository;
    private final ItemSearchKeywordService itemSearchKeywordService;

    @Transactional
    public ItemBriefDto addItem(ItemCreateRequest itemCreateRequest, Long userId) {
        Category category = categoryRepository.findById(itemCreateRequest.getCategoryId())
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));
        Store store = storeRepository.findById(itemCreateRequest.getStoreId())
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));

        Item item = itemRepository.save(Item.of(category, store, itemCreateRequest, userId));

        itemCreateRequest.getItemOptions().stream()
                .map(itemOptionDto -> ItemOption.of(item, itemOptionDto))
                .forEach(item::addItemOption);

        category.increaseItemCount(1);
        itemSearchKeywordService.initDefaultSearchKeywords(item.getId());

        return ItemBriefDto.builder().item(item).build();
    }

    @Transactional
    public ItemOption addStocks(Long itemOptionId, int amount) {
        if (amount <= 0 || itemOptionId == null || !itemOptionRepository.existsById(itemOptionId)) {
            throw new ApiException(ErrorEnum.INVALID_REQUEST);
        }
        itemOptionRepository.updateStocksCount(itemOptionId, amount);
        return itemOptionRepository.findById(itemOptionId).get();
    }

    @Transactional
    public void updateToSoldOut(Long itemOptionId, Boolean isTemporarily) {
        ItemOption itemOption = itemOptionRepository.findById(itemOptionId)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));

        itemOption.beSoldOut(isTemporarily);
        itemOption.getItem().renewSaleState();
    }

    @Transactional
    public void endProduction(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));

        item.endProduction();
    }

    @Transactional
    public List<ItemOption> startSale(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));

        return item.startAllSale();
    }

    @Transactional
    public void restartSale(Long itemOptionId) {
        ItemOption itemOption = itemOptionRepository.findById(itemOptionId)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));
        boolean hasSucceed = itemOption.startSale();
        if (!hasSucceed) {
            throw new ApiException(ErrorEnum.OUT_OF_STOCK);
        }
    }

    @Transactional
    public void modifyOrderCount(Long itemId, Long orderCount) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));
        item.modifyOrderCount(orderCount);
    }

    @Transactional
    @CacheEvict(cacheNames = "STOCKS", key = "#itemOptionId")
    public Long reduceStock(Long itemOptionId, Integer amount) {
        ItemOption itemOption = itemOptionRepository.findById(itemOptionId)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));
        Long stocksAfter = itemOption.reduceStocksCount(amount);
        itemOption.getItem().renewSaleState();
        return stocksAfter;
    }
}
