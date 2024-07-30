package com.example.flab.soft.shoppingmallfashion.order.controller;

import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetails.AuthUser;
import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import com.example.flab.soft.shoppingmallfashion.order.service.OrderInfoDto;
import com.example.flab.soft.shoppingmallfashion.order.service.OrderService;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public SuccessResult<OrderInfoDto> order(
            @Validated @RequestBody OrderRequest orderRequest,
            @AuthenticationPrincipal AuthUser authUser) {
        OrderInfoDto orderInfoDto = orderService.order(orderRequest, authUser.getId());
        return SuccessResult.<OrderInfoDto>builder().response(orderInfoDto).build();
    }

    @PatchMapping("/{orderId}")
    public SuccessResult<Void> cancelOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal AuthUser authUser) {
        orderService.cancelOrder(orderId, authUser.getId());
        return SuccessResult.<Void>builder().build();
    }
}
