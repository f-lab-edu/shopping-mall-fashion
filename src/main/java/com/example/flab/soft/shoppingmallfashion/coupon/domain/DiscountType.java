package com.example.flab.soft.shoppingmallfashion.coupon.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public enum DiscountType {
    FIXED_DISCOUNT_KRW(new FixedDiscountStrategy(), "", DiscountUnit.KRW), // 원화 고정 할인
    RATE_DISCOUNT(new RateDiscountStrategy(), "", DiscountUnit.PERCENTAGE), // 비율 할인
    ;

    private final DiscountStrategy discountStrategy;
    private final String name;
    private final DiscountUnit discountUnit;

    DiscountType(DiscountStrategy discountStrategy, String name, DiscountUnit discountUnit) {
        this.discountStrategy = discountStrategy;
        this.name = name;
        this.discountUnit = discountUnit;
    }

    public DiscountDetails calculate(Integer beforePrice, Integer discountAmount) {
        return discountStrategy.calculate(beforePrice, discountAmount);
    }
}
