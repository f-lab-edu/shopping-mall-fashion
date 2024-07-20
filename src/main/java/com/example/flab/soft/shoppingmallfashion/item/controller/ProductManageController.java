package com.example.flab.soft.shoppingmallfashion.item.controller;

import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import com.example.flab.soft.shoppingmallfashion.item.service.ItemCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/item/management/product")
@RequiredArgsConstructor
public class ProductManageController {
    private final ItemCommandService itemService;

    @PatchMapping("/sale-state/{productId}")
    public SuccessResult<Void> soldOut(
            @PathVariable Long productId) {
        itemService.updateToSoldOut(productId);
        return SuccessResult.<Void>builder().build();
    }
}
