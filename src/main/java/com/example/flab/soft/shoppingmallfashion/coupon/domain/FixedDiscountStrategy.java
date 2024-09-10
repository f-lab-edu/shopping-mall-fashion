package com.example.flab.soft.shoppingmallfashion.coupon.domain;

public class FixedDiscountStrategy implements DiscountStrategy {
    @Override
    public DiscountDetails calculate(Integer beforePrice, Integer discountAmounts) {
        return DiscountDetails.builder()
                .discountedAmounts(discountAmounts)
                .priceAfterDiscount(beforePrice - discountAmounts)
                .build();
    }
}
