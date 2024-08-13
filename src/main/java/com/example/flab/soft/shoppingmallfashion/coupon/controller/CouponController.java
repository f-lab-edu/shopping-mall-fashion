package com.example.flab.soft.shoppingmallfashion.coupon.controller;

import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetails.AuthUser;
import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import com.example.flab.soft.shoppingmallfashion.coupon.service.CouponInfo;
import com.example.flab.soft.shoppingmallfashion.coupon.service.CouponRedissonService;
import com.example.flab.soft.shoppingmallfashion.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;
    private final CouponRedissonService couponRedissonService;

    @PostMapping("/{couponId}")
    public SuccessResult<CouponInfo> acquireCoupon(
            @PathVariable Long couponId,
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam(defaultValue = "shared") LockType lockType) {
        CouponInfo couponInfo;
        if (lockType == LockType.SHARED) {
            couponInfo = couponRedissonService.getCoupon(authUser.getId(), couponId);
        } else {
            couponInfo = couponService.acquireCoupon(authUser.getId(), couponId);
        }
        return SuccessResult.<CouponInfo>builder().response(couponInfo).build();
    }
}
