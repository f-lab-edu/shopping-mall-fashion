package com.example.flab.soft.shoppingmallfashion.order.controller;

import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetails.AuthUser;
import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import com.example.flab.soft.shoppingmallfashion.order.service.OrderInfoDto;
import com.example.flab.soft.shoppingmallfashion.order.service.OrderService;
import io.hackle.sdk.HackleClient;
import io.hackle.sdk.common.Event;
import io.hackle.sdk.common.User;
import jakarta.servlet.http.HttpSession;
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
    private final HackleClient hackleClient;

    @PostMapping
    public SuccessResult<OrderInfoDto> order(
            @Validated @RequestBody OrderRequest orderRequest,
            @AuthenticationPrincipal AuthUser authUser, HttpSession httpSession) {
        OrderInfoDto orderInfoDto = orderService.order(orderRequest, authUser.getId());

        User user = User.builder()
                .userId(httpSession.getId())
                .build();

        Event event = Event.builder("order")
                .property("pay_amount", orderInfoDto.getAmount())
                .property("item_id", orderInfoDto.getItemId())
                .build();

        hackleClient.track(event, user);

        return SuccessResult.<OrderInfoDto>builder().response(orderInfoDto).build();
    }

    @PatchMapping("/{orderId}")
    public SuccessResult<Void> cancelOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal AuthUser authUser) {
        orderService.cancelOrder(orderId, authUser.getId());
        return SuccessResult.<Void>builder().build();
    }

    @PatchMapping("/{orderId}/delivery-info")
    public SuccessResult<Void> changeDeliveryInfo(
            @RequestBody DeliveryInfoUpdateRequest updateRequest,
            @PathVariable Long orderId,
            @AuthenticationPrincipal AuthUser authUser) {
        orderService.changeDeliveryInfo(updateRequest, orderId, authUser.getId());
        return SuccessResult.<Void>builder().build();
    }
}
