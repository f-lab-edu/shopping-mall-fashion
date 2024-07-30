package com.example.flab.soft.shoppingmallfashion.coupon;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private static final int VALIDATION_IN_MONTHS = 3;

    public Integer issueCoupons(DiscountType discountType,
                             Integer discountedAmount,
                             DiscountUnit discountUnit,
                             Integer issuedAmount) {
        List<Coupon> couponList = Stream.generate(() -> new Coupon(Discount.builder()
                        .discountType(discountType)
                        .amount(discountedAmount)
                        .discountUnit(discountUnit)
                        .build(), LocalDateTime.now().plusMonths(VALIDATION_IN_MONTHS)))
                .limit(issuedAmount)
                .toList();
        return couponRepository.saveAll(couponList).size();
    }
}
