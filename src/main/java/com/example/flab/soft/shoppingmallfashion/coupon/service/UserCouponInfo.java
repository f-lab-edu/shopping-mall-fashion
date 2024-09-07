package com.example.flab.soft.shoppingmallfashion.coupon.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCouponInfo {
    private CouponInfo couponInfo;
    private Integer quantity;
    private List<LocalDateTime> expiredAt;
}
