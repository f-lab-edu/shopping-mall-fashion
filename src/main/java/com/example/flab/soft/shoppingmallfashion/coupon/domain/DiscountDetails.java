package com.example.flab.soft.shoppingmallfashion.coupon.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscountDetails {
    private Integer discountedAmounts;
    private Integer priceAfterDiscount;
}
