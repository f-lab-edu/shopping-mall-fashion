package com.example.flab.soft.shoppingmallfashion.coupon;

import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetails.AuthUser;
import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @PostMapping
    public SuccessResult<CouponInfo> getCoupon(
            @RequestBody String couponName,
            @AuthenticationPrincipal AuthUser authUser) {
        CouponInfo couponInfo = couponService.getCoupon(authUser.getId(), couponName);
        return SuccessResult.<CouponInfo>builder().response(couponInfo).build();
    }
}
