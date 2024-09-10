package com.example.flab.soft.shoppingmallfashion.coupon.domain;

public class RateDiscountStrategy implements DiscountStrategy {
    @Override
    public DiscountDetails calculate(Integer beforePrice, Integer rate) {
        Integer discountedAmounts = beforePrice * rate / 100;
        return DiscountDetails.builder()
                .discountedAmounts(discountedAmounts)
                .priceAfterDiscount(beforePrice - discountedAmounts)
                .build();
    }
}
