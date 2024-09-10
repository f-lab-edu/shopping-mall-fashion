package com.example.flab.soft.shoppingmallfashion.coupon.controller;

import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetails.AuthUser;
import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import com.example.flab.soft.shoppingmallfashion.coupon.service.CouponInfo;
import com.example.flab.soft.shoppingmallfashion.coupon.service.CouponRedissonService;
import com.example.flab.soft.shoppingmallfashion.coupon.service.CouponService;
import com.example.flab.soft.shoppingmallfashion.coupon.service.UserCouponInfo;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;
    private final CouponRedissonService couponRedissonService;

    @PostMapping("/db/{couponId}")
    public SuccessResult<CouponInfo> acquireCoupon(
            @PathVariable Long couponId,
            @AuthenticationPrincipal AuthUser authUser) {
        CouponInfo couponInfo = couponService.acquireCouponPessimistic(authUser.getId(), couponId);
        return SuccessResult.<CouponInfo>builder().response(couponInfo).build();
    }

    @PostMapping("/redisson/{couponId}")
    public SuccessResult<CouponInfo> acquireCouponByRedisson(
            @PathVariable Long couponId,
            @AuthenticationPrincipal AuthUser authUser) {
        CouponInfo couponInfo = couponRedissonService.acquireCoupon(authUser.getId(), couponId);
        return SuccessResult.<CouponInfo>builder().response(couponInfo).build();
    }

    @GetMapping
    public SuccessResult<List<UserCouponInfo>> retrieveAllCoupons(
            @AuthenticationPrincipal AuthUser authUser) {
        List<UserCouponInfo> userCouponInfos = couponService.findAllCouponsByUserId(authUser.getId());
        return SuccessResult.<List<UserCouponInfo>>builder().response(userCouponInfos).build();
    }
}
