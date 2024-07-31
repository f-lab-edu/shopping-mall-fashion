package com.example.flab.soft.shoppingmallfashion.coupon;

import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private static final int VALIDATION_IN_MONTHS = 3;

    @Transactional
    public Integer issueCoupons(String couponName,
                                DiscountType discountType, Integer discountedAmount,
                             DiscountUnit discountUnit, Integer issuedAmount) {
        List<Coupon> couponList = Stream.generate(() -> Coupon.builder()
                        .name(couponName)
                        .discount(Discount.builder()
                                .discountType(discountType)
                                .amount(discountedAmount)
                                .discountUnit(discountUnit)
                                .build())
                        .expiration(LocalDateTime.now().plusMonths(VALIDATION_IN_MONTHS))
                        .build())
                .limit(issuedAmount)
                .toList();
        return couponRepository.saveAll(couponList).size();
    }

    @Transactional
    public CouponInfo getCoupon(Long userId, String couponName) {
        List<Coupon> coupons = couponRepository
                .findFirstUnownedCouponByName(couponName, PageRequest.of(0, 1));
        if (coupons.isEmpty()) {
            throw new ApiException(ErrorEnum.OUT_OF_COUPON);
        }
        Coupon coupon = coupons.get(0);
        coupon.decideOwner(userId);
        return CouponInfo.builder().coupon(coupon).build();
    }
}
