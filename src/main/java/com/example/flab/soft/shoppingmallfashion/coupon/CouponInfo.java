package com.example.flab.soft.shoppingmallfashion.coupon;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CouponInfo {
    private Long couponId;
    private String couponName;
    private LocalDateTime expiredAt;

    @Builder
    public CouponInfo(Coupon coupon) {
        this.couponId = coupon.getId();
        this.couponName = coupon.getName();
        this.expiredAt = coupon.getExpiration();
    }
}
