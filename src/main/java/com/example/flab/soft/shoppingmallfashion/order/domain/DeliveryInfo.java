package com.example.flab.soft.shoppingmallfashion.order.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class DeliveryInfo {
    private String recipientName;
    private String roadAddress;
    private String addressDetail;
}
