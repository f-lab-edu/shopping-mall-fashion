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
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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
    private final RedissonClient redissonClient;

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
        initDefaultSearchKeyword(item);

        return ItemBriefDto.builder().item(item).build();
    }

    private void initDefaultSearchKeyword(Item item) {
        RLock lock = redissonClient.getLock("search-keyword");
        try {
            boolean acquireLock = lock.tryLock(10, 3, TimeUnit.SECONDS);
            log.debug("Lock acquired by item Id: {}", item.getId());
            if (!acquireLock) {
                throw new ApiException(ErrorEnum.RETRY);
            }
            itemSearchKeywordService.initDefaultSearchKeywords(item.getId());
        } catch (InterruptedException e) {
            throw new ApiException(ErrorEnum.RETRY);
        } finally {
            lock.unlock();
            log.debug("Lock released by item Id: {}", item.getId());
        }
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
}
