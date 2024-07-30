package com.example.flab.soft.shoppingmallfashion.coupon;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CouponServiceTest {
    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponRepository couponRepository;
    private static final int ISSUED_AMOUNT = 100;

    @Test
    @DisplayName("쿠폰 100개 발행")
    void issueCoupons() {
        Integer issuedCouponsCount = couponService.issueCoupons(DiscountType.FIXED_PRICE_DISCOUNT,
                1000, DiscountUnit.KRW, ISSUED_AMOUNT);

        assertThat(issuedCouponsCount).isEqualTo(ISSUED_AMOUNT);
        assertThat(couponRepository.count()).isEqualTo(ISSUED_AMOUNT);
    }
}