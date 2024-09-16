package com.example.flab.soft.shoppingmallfashion.item.controller;

import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import com.example.flab.soft.shoppingmallfashion.item.service.ItemBriefDto;
import com.example.flab.soft.shoppingmallfashion.item.service.ItemDetailsDto;
import com.example.flab.soft.shoppingmallfashion.item.service.ItemQueryService;
import com.example.flab.soft.shoppingmallfashion.item.service.ItemSearchKeywordDto;
import com.example.flab.soft.shoppingmallfashion.item.service.ItemSearchKeywordService;
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
    private final ItemSearchKeywordService itemSearchKeywordService;

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
    public SuccessResult<ItemDto> getItemDetails(
            @PathVariable Long itemId) {
        ItemDetailsDto itemDetails = itemService.getItemDetails(itemId);
        ItemDto itemDto = ItemDto.builder()
                .itemDetailsDto(itemDetails)
                .sameStoreItems(itemService.getSameStoreItems(itemDetails.getItemBriefDto().getStoreId()))
                .sameCategoryItems(itemService.getSameCategoryItems(itemDetails.getItemBriefDto().getCategoryId()))
                .relatedItems(itemService.getRelatedItems(itemId))
                .build();
        return SuccessResult.<ItemDto>builder().response(itemDto).build();
    }
}
