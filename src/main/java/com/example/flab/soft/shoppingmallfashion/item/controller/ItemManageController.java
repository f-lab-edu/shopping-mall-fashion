package com.example.flab.soft.shoppingmallfashion.item.controller;

import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetails.AuthUser;
import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import com.example.flab.soft.shoppingmallfashion.item.service.ItemCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/crew/item")
@RequiredArgsConstructor
public class ItemManageController {
    private final ItemCommandService itemService;
    @PostMapping("")
    public SuccessResult<Void> newItem(
            @RequestBody @Validated ItemCreateRequest itemCreateRequest,
            @AuthenticationPrincipal AuthUser authUser) {
        itemService.addItem(itemCreateRequest, authUser.getId());
        return SuccessResult.<Void>builder().build();
    }

    @PatchMapping("/{itemId}/sale-state/end-of-production")
    public SuccessResult<Void> endProduction(
            @PathVariable Long itemId) {
        itemService.endProduction(itemId);
        return SuccessResult.<Void>builder().build();
    }

    @PatchMapping("/{itemId}/sale-state/on-sale")
    public SuccessResult<Void> startSale(
            @PathVariable Long itemId) {
        itemService.startSale(itemId);
        return SuccessResult.<Void>builder().build();
    }
}
