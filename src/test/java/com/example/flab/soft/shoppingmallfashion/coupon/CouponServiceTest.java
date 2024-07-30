package com.example.flab.soft.shoppingmallfashion.coupon;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CouponServiceTest {
    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponRepository couponRepository;
    private static final int ISSUED_AMOUNT = 100;

    @Test
    @DisplayName("쿠폰 100개 발행")
    void issueCoupons() {
        Integer issuedCouponsCount = couponService.issueCoupons("welcomeCoupon",
                DiscountType.FIXED_PRICE_DISCOUNT,
                1000, DiscountUnit.KRW, ISSUED_AMOUNT);

        assertThat(issuedCouponsCount).isEqualTo(ISSUED_AMOUNT);
        assertThat(couponRepository.count()).isEqualTo(ISSUED_AMOUNT);
    }

    @Test
    @DisplayName("쿠폰 제공")
    void getCoupons() {
        Long userId = 1L;
        Coupon coupon = couponRepository.save(Coupon.builder()
                .name("welcomeCoupon")
                .discount(Discount.builder()
                        .discountType(DiscountType.FIXED_PRICE_DISCOUNT)
                        .amount(1000)
                        .discountUnit(DiscountUnit.KRW)
                        .build())
                .expiration(LocalDateTime.now().plusMonths(3))
                .build());

        CouponInfo couponInfo = couponService.getCoupon(userId, "welcomeCoupon");
        assertThat(coupon.getOwnerId()).isEqualTo(userId);
        assertThat(coupon.getName()).isEqualTo(couponInfo.getCouponName());
    }
}