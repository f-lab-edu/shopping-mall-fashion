package com.example.flab.soft.shoppingmallfashion.coupon.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class Discount {
    @Enumerated(value = EnumType.STRING)
    private DiscountType discountType;
    private Integer discountAmount;

    public DiscountDetails calculate(Integer beforePrice) {
        return discountType.calculate(beforePrice, discountAmount);
    }
}
