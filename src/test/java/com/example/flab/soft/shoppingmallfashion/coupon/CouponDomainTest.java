package com.example.flab.soft.shoppingmallfashion.coupon;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.flab.soft.shoppingmallfashion.coupon.domain.Coupon;
import com.example.flab.soft.shoppingmallfashion.coupon.domain.Discount;
import com.example.flab.soft.shoppingmallfashion.coupon.domain.DiscountDetails;
import com.example.flab.soft.shoppingmallfashion.coupon.domain.DiscountType;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CouponDomainTest {
    @Test
    @DisplayName("고정 할인 금액 계산")
    void calculateFixedDiscountAmounts() {
        //given
        Coupon coupon = Coupon.builder()
                .name("welcomeCoupon")
                .amounts(1L)
                .discount(Discount.builder()
                        .discountType(DiscountType.FIXED_DISCOUNT_KRW)
                        .discountAmount(1000)
                        .build())
                .validation(Duration.of(3, ChronoUnit.DAYS))
                .build();
        assertThat(coupon.calculateDiscount(10000)).usingRecursiveComparison()
                .isEqualTo(DiscountDetails.builder()
                        .discountedAmounts(1000)
                        .priceAfterDiscount(9000)
                        .build());
    }

    @Test
    @DisplayName("비율 할인 금액 계산")
    void calculateRateDiscountAmounts() {
        //given
        Coupon coupon = Coupon.builder()
                .name("welcomeCoupon")
                .amounts(1L)
                .discount(Discount.builder()
                        .discountType(DiscountType.RATE_DISCOUNT)
                        .discountAmount(10)
                        .build())
                .validation(Duration.of(3, ChronoUnit.DAYS))
                .build();
        assertThat(coupon.calculateDiscount(10000)).usingRecursiveComparison()
                .isEqualTo(DiscountDetails.builder()
                        .discountedAmounts(1000)
                        .priceAfterDiscount(9000)
                        .build());
    }
}
