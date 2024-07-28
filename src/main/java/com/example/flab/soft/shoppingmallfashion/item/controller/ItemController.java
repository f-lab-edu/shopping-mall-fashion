package com.example.flab.soft.shoppingmallfashion.item.controller;

import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import com.example.flab.soft.shoppingmallfashion.item.service.ItemBriefDto;
import com.example.flab.soft.shoppingmallfashion.item.service.ItemQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
