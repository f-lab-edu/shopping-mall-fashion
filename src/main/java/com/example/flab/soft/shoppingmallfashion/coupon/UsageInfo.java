package com.example.flab.soft.shoppingmallfashion.coupon;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsageInfo {
    private Boolean used;
    private Long usedItemId;
    private Long usedOrderId;
}
