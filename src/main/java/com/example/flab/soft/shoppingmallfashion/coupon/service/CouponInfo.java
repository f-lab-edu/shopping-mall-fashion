package com.example.flab.soft.shoppingmallfashion.coupon.service;

import com.example.flab.soft.shoppingmallfashion.coupon.domain.Coupon;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CouponInfo {
    private Long couponId;
    private String couponName;

    @Builder
    public CouponInfo(Coupon coupon) {
        this.couponId = coupon.getId();
        this.couponName = coupon.getName();
    }
}
