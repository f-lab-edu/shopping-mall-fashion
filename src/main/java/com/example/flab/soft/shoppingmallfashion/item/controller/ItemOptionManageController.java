package com.example.flab.soft.shoppingmallfashion.item.controller;

import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import com.example.flab.soft.shoppingmallfashion.item.domain.ItemOption;
import com.example.flab.soft.shoppingmallfashion.item.service.ItemCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/crew/item/item-option")
@RequiredArgsConstructor
public class ItemOptionManageController {
    private final ItemCommandService itemService;

    @PatchMapping("{itemOptionId}/sale-state/sold-out")
    public SuccessResult<Void> soldOut(
            @PathVariable Long itemOptionId,
            @RequestParam Boolean isTemporarily) {
        itemService.updateToSoldOut(itemOptionId, isTemporarily);
        return SuccessResult.<Void>builder().build();
    }

    @PatchMapping("/{itemOptionId}/sale-state/on-sale")
    public SuccessResult<Void> startSale(
            @PathVariable Long itemOptionId) {
        itemService.restartSale(itemOptionId);
        return SuccessResult.<Void>builder().build();
    }

    @PatchMapping("/{itemOptionId}/stocks-count")
    public SuccessResult<ItemOption> addStocks(
            @PathVariable Long itemOptionId, @RequestBody StocksAddRequest stocksAddRequest) {
        ItemOption itemOption =
                itemService.addStocks(itemOptionId, stocksAddRequest.getAmount());
        return SuccessResult.<ItemOption>builder().response(itemOption).build();
    }
}
