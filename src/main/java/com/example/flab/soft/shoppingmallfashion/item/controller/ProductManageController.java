package com.example.flab.soft.shoppingmallfashion.item.controller;

import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import com.example.flab.soft.shoppingmallfashion.item.service.ItemCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/crew/item/product")
@RequiredArgsConstructor
public class ProductManageController {
    private final ItemCommandService itemService;

    @PatchMapping("{productId}/sale-state/sold-out")
    public SuccessResult<Void> soldOut(
            @PathVariable Long productId,
            @RequestParam Boolean isTemporarily) {
        itemService.updateToSoldOut(productId, isTemporarily);
        return SuccessResult.<Void>builder().build();
    }

    @PatchMapping("/{productId}/sale-state/on-sale")
    public SuccessResult<Void> startSale(
            @PathVariable Long productId) {
        itemService.restartSale(productId);
        return SuccessResult.<Void>builder().build();
    }
}
