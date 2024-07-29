package com.example.flab.soft.shoppingmallfashion.order.service;

import com.example.flab.soft.shoppingmallfashion.item.domain.ItemOption;
import com.example.flab.soft.shoppingmallfashion.order.domain.Order;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderInfoDto {
    private Long orderId;
    private Long itemId;
    private Long itemOptionId;
    private String itemName;
    private String itemOptionName;
    private Integer amount;
    private String recipientName;
    private String roadAddress;
    private String addressDetail;

    @Builder
    public OrderInfoDto(Order order, ItemOption itemOption) {
        this.orderId = order.getId();
        this.itemId = itemOption.getItem().getId();
        this.itemOptionId = itemOption.getId();
        this.itemName = itemOption.getItem().getName();
        this.itemOptionName = itemOption.getName();
        this.amount = order.getAmount();
        this.recipientName = order.getDeliveryInfo().getRecipientName();
        this.roadAddress = order.getDeliveryInfo().getRoadAddress();
        this.addressDetail = order.getDeliveryInfo().getAddressDetail();
    }
}
