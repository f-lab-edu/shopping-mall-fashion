package com.example.flab.soft.shoppingmallfashion.item.controller;

import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import com.example.flab.soft.shoppingmallfashion.item.service.ItemBriefDto;
import com.example.flab.soft.shoppingmallfashion.item.service.ItemBriefDtos;
import com.example.flab.soft.shoppingmallfashion.item.service.ItemDetailsDto;
import com.example.flab.soft.shoppingmallfashion.item.service.ItemOptionStocksDto;
import com.example.flab.soft.shoppingmallfashion.item.service.ItemQueryService;
import com.example.flab.soft.shoppingmallfashion.item.service.ItemsCountDto;
import java.util.List;

import io.hackle.sdk.HackleClient;
import io.hackle.sdk.common.Event;
import io.hackle.sdk.common.User;
import io.hackle.sdk.common.Variation;
import jakarta.servlet.http.HttpSession;
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
    private final HackleClient hackleClient;

    @GetMapping
    public SuccessResult<Page<ItemBriefDto>> getItems(
            @PageableDefault(
                    size = 50, sort = "itemStats.orderCount",
                    direction = Direction.DESC) Pageable pageable,
            ItemListRequest itemListRequest, HttpSession httpSession) {
        Page<ItemBriefDto> items = itemService.getItems(
                itemListRequest.getMinPrice(), itemListRequest.getMaxPrice(),
                itemListRequest.getCategoryId(), itemListRequest.getStoreId(),
                itemListRequest.getSex(), pageable);

        User user = User.builder()
                .userId(httpSession.getId())
                .build();

        Event event = Event.builder("searched_item_list")
                .property("category_id", itemListRequest.getCategoryId())
                .property("store_id", itemListRequest.getStoreId())
                .property("page", pageable.getPageNumber())
                .build();

        hackleClient.track("$page_view", user);
        hackleClient.track(event, user);

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
            @PathVariable Long itemId, HttpSession httpSession) {
        ItemDetailsDto itemDetails = itemService.getItemDetails(itemId);
        User user = User.builder()
                .userId(httpSession.getId())
                .build();

        Event event = Event.builder("searched_item")
                .property("item_id", String.valueOf(itemId))
                .property("category_id", itemDetails.getItemBriefDto().getCategoryId())
                .property("store_id", itemDetails.getItemBriefDto().getStoreId())
                .property("sale_price", itemDetails.getItemBriefDto().getSalePrice())
                .build();

        Variation variation = hackleClient.variation(5, user);

        if (variation == Variation.A) {
            // DO A
        } else {
            // DO B
        }

        hackleClient.track(event, user);
        hackleClient.track("$page_view", user);
        return SuccessResult.<ItemDetailsDto>builder()
                .response(itemDetails)
                .build();
    }

    @GetMapping("/{itemId}/{itemOptionId}/stocks")
    public SuccessResult<ItemOptionStocksDto> getStocks(
            @PathVariable Long itemOptionId) {
        return SuccessResult.<ItemOptionStocksDto>builder()
                .response(itemService.getStocks(itemOptionId))
                .build();
    }

    @GetMapping("/top-items-by-store")
    public SuccessResult<ItemBriefDtos> getTopItemsByStore(
            @RequestParam Long storeId) {
        return SuccessResult.<ItemBriefDtos>builder()
                .response(itemService.getTopItemsByStore(storeId))
                .build();
    }

    @GetMapping("/top-items-by-category")
    public SuccessResult<ItemBriefDtos> getTopItemsByCategory(
            @RequestParam Long categoryId) {
        return SuccessResult.<ItemBriefDtos>builder()
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
