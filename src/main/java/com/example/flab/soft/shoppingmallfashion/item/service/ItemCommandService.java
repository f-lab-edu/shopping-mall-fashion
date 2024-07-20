package com.example.flab.soft.shoppingmallfashion.item.service;

import com.example.flab.soft.shoppingmallfashion.category.Category;
import com.example.flab.soft.shoppingmallfashion.category.CategoryRepository;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.item.controller.ItemCreateRequest;
import com.example.flab.soft.shoppingmallfashion.item.domain.Item;
import com.example.flab.soft.shoppingmallfashion.item.domain.Product;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemRepository;
import com.example.flab.soft.shoppingmallfashion.item.repository.ProductRepository;
import com.example.flab.soft.shoppingmallfashion.store.repository.Store;
import com.example.flab.soft.shoppingmallfashion.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemCommandService {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Long addItem(ItemCreateRequest itemCreateRequest, Long userId) {
        Category category = categoryRepository.findById(itemCreateRequest.getCategoryId())
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));
        Store store = storeRepository.findById(itemCreateRequest.getStoreId())
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));

        Item item = itemRepository.save(Item.of(category, store, itemCreateRequest, userId));

        itemCreateRequest.getProducts().stream()
                .map(productDto -> Product.of(item, productDto))
                .forEach(item::addProduct);

        category.increaseItemCount(1);
        return item.getId();
    }

    @Transactional
    public void updateToSoldOut(Long productId, Boolean isTemporarily) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));

        product.beSoldOut(isTemporarily);
    }

    @Transactional
    public void endProduction(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));

        item.endProduction();
    }

    @Transactional
    public void startSale(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));

        item.startAllSale();
    }

    @Transactional
    public void restartSale(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));
        product.startSale();
    }
}
