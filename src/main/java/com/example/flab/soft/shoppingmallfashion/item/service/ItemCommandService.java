package com.example.flab.soft.shoppingmallfashion.item.service;

import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.item.controller.ItemCreateRequest;
import com.example.flab.soft.shoppingmallfashion.item.domain.Category;
import com.example.flab.soft.shoppingmallfashion.item.domain.Item;
import com.example.flab.soft.shoppingmallfashion.item.domain.SaleState;
import com.example.flab.soft.shoppingmallfashion.item.domain.Sex;
import com.example.flab.soft.shoppingmallfashion.item.repository.CategoryRepository;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemRepository;
import com.example.flab.soft.shoppingmallfashion.store.repository.Store;
import com.example.flab.soft.shoppingmallfashion.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemCommandService {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;

    public void addItem(ItemCreateRequest itemCreateRequest, Long userId) {
        Category category = categoryRepository.findById(itemCreateRequest.getCategoryId())
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));
        Store store = storeRepository.findById(itemCreateRequest.getStoreId())
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));

        itemRepository.save(Item.builder()
                .name(itemCreateRequest.getName())
                .price(itemCreateRequest.getPrice())
                .discountAppliedPrice(itemCreateRequest.getDiscountAppliedPrice())
                .description(itemCreateRequest.getDescription())
                .sex(Sex.valueOf(itemCreateRequest.getSex()))
                .saleState(SaleState.valueOf(itemCreateRequest.getSaleState()))
                .store(store)
                .category(category)
                .lastlyModifiedBy(userId)
                .build());
    }
}
