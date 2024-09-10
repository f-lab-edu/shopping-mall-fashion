package com.example.flab.soft.shoppingmallfashion.order.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsedCouponInfo {
    private Long usedUserCouponId;
    private String usedCouponName;
}
