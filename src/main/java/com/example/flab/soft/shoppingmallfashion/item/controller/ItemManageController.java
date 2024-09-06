package com.example.flab.soft.shoppingmallfashion.item.controller;

import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetails.AuthUser;
import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import com.example.flab.soft.shoppingmallfashion.item.domain.ItemOption;
import com.example.flab.soft.shoppingmallfashion.item.service.ItemBriefDto;
import com.example.flab.soft.shoppingmallfashion.item.service.ItemCommandService;
import com.example.flab.soft.shoppingmallfashion.item.service.ItemSearchKeywordDto;
import com.example.flab.soft.shoppingmallfashion.item.service.ItemSearchKeywordService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/crew/item")
@RequiredArgsConstructor
public class ItemManageController {
    private final ItemCommandService itemService;
    private final ItemSearchKeywordService itemSearchKeywordService;
    @PostMapping
    public SuccessResult<ItemBriefDto> newItem(
            @RequestBody @Validated ItemCreateRequest itemCreateRequest,
            @AuthenticationPrincipal AuthUser authUser) {
        return SuccessResult.<ItemBriefDto>builder()
                .response(itemService.addItem(itemCreateRequest, authUser.getId()))
                .build();
    }

    @PatchMapping("/{itemId}/sale-state/end-of-production")
    public SuccessResult<Void> endProduction(
            @PathVariable Long itemId) {
        itemService.endProduction(itemId);
        return SuccessResult.<Void>builder().build();
    }

    @PatchMapping("/{itemId}/sale-state/on-sale")
    public SuccessResult<List<ItemOption>> startSale(
            @PathVariable Long itemId) {
        List<ItemOption> itemOptions = itemService.startSale(itemId);
        return SuccessResult.<List<ItemOption>>builder().response(itemOptions).build();
    }

    @PutMapping("/{itemId}/item-search-keywords")
    public SuccessResult<ItemSearchKeywordDto> putItemSearchKeywords(
            @PathVariable Long itemId,
            @RequestBody ItemSearchKeywordsPutRequest itemSearchKeywordsPutRequest) {
        ItemSearchKeywordDto itemSearchKeywordDto = itemSearchKeywordService.updateItemSearchKeyword(
                itemId, itemSearchKeywordsPutRequest.getItemSearchKeywords());
        return SuccessResult.<ItemSearchKeywordDto>builder()
                .response(itemSearchKeywordDto).build();
    }

    @GetMapping("/{itemId}/item-search-keywords")
    public SuccessResult<ItemSearchKeywordDto> getItemSearchKeywords(
            @PathVariable Long itemId) {
        return SuccessResult.<ItemSearchKeywordDto>builder()
                .response(itemSearchKeywordService.getAllSearchKeywords(itemId)).build();
    }
}
