package com.example.flab.soft.shoppingmallfashion.item.controller;

import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import com.example.flab.soft.shoppingmallfashion.item.service.ItemBriefDto;
import com.example.flab.soft.shoppingmallfashion.item.service.ItemDetailsDto;
import com.example.flab.soft.shoppingmallfashion.item.service.ItemQueryService;
import com.example.flab.soft.shoppingmallfashion.item.service.ItemsCountDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemQueryService itemService;

    @GetMapping
    public SuccessResult<Page<ItemBriefDto>> getItems(
            @PageableDefault(
                    size = 50, sort = "itemStats.orderCount",
                    direction = Direction.DESC) Pageable pageable,
            ItemListRequest itemListRequest) {
        Page<ItemBriefDto> items = itemService.getItems(
                itemListRequest.getMinPrice(), itemListRequest.getMaxPrice(),
                itemListRequest.getCategoryId(), itemListRequest.getStoreId(),
                itemListRequest.getSex(), pageable);
        return SuccessResult.<Page<ItemBriefDto>>builder().response(items).build();
    }

    @GetMapping("/count")
    public SuccessResult<ItemsCountDto> getItemsCount(
            ItemListRequest itemListRequest) {
        return SuccessResult.<ItemsCountDto>builder()
                .response(itemService.getItemCounts(
                            itemListRequest.getMinPrice(), itemListRequest.getMaxPrice(),
                            itemListRequest.getCategoryId(), itemListRequest.getStoreId(),
                            itemListRequest.getSex()))
                .build();
    }

    @GetMapping("/items-with-keyword")
    public SuccessResult<Page<ItemBriefDto>> getItemsWithKeyword(
            @PageableDefault(
                    size = 50, sort = "itemStats.orderCount",
                    direction = Direction.DESC) Pageable pageable,
            @RequestParam String keyword) {
        Page<ItemBriefDto> items = itemService.getItemsWithKeyword(keyword, pageable);
        return SuccessResult.<Page<ItemBriefDto>>builder().response(items).build();
    }

    @GetMapping("/{itemId}")
    public SuccessResult<ItemDetailsDto> getItemDetails(
            @PathVariable Long itemId) {
        return SuccessResult.<ItemDetailsDto>builder()
                .response(itemService.getItemDetails(itemId))
                .build();
    }

    @GetMapping("/top-items-by-store")
    public SuccessResult<List<ItemBriefDto>> getTopItemsByStore(
            @RequestParam Long storeId) {
        return SuccessResult.<List<ItemBriefDto>>builder()
                .response(itemService.getTopItemsByStore(storeId))
                .build();
    }

    @GetMapping("/top-items-by-category")
    public SuccessResult<List<ItemBriefDto>> getTopItemsByCategory(
            @RequestParam Long categoryId) {
        return SuccessResult.<List<ItemBriefDto>>builder()
                .response(itemService.getTopItemsByCategory(categoryId))
                .build();
    }

    @GetMapping("/{itemId}/top-related-items")
    public SuccessResult<List<ItemBriefDto>> getRelatedItems(
            @PathVariable Long itemId) {
        return SuccessResult.<List<ItemBriefDto>>builder()
                .response(itemService.getRelatedItems(itemId))
                .build();
    }
}
