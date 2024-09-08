package com.example.flab.soft.shoppingmallfashion.coupon.domain;

@FunctionalInterface
public interface DiscountStrategy {
    DiscountDetails calculate(Integer beforePrice, Integer discountAmounts);
}
