package com.example.flab.soft.shoppingmallfashion.coupon;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Embeddable
public class Discount {
    @Enumerated(value = EnumType.STRING)
    private DiscountType discountType;
    private Integer amount;
    @Enumerated(value = EnumType.STRING)
    private DiscountUnit discountUnit;
}
