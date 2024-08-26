package com.example.flab.soft.shoppingmallfashion.coupon.service;

import com.example.flab.soft.shoppingmallfashion.coupon.repository.CouponRepository;
import com.example.flab.soft.shoppingmallfashion.coupon.repository.UserCouponRepository;
import com.example.flab.soft.shoppingmallfashion.coupon.domain.Coupon;
import com.example.flab.soft.shoppingmallfashion.coupon.domain.Discount;
import com.example.flab.soft.shoppingmallfashion.coupon.domain.DiscountType;
import com.example.flab.soft.shoppingmallfashion.coupon.domain.DiscountUnit;
import com.example.flab.soft.shoppingmallfashion.coupon.domain.UserCoupon;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import java.time.Duration;
import java.time.temporal.TemporalUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    @Transactional
    public Long issueCoupons(String couponName,
                             DiscountType discountType, Integer discountedAmount,
                             DiscountUnit discountUnit, Long issuedAmount,
                             Long validationAmount, TemporalUnit validationUnit) {
        Coupon coupon = Coupon.builder()
                .name(couponName)
                .amounts(issuedAmount)
                .discount(Discount.builder()
                        .discountType(discountType)
                        .discountAmount(discountedAmount)
                        .discountUnit(discountUnit)
                        .build())
                .validation(Duration.of(validationAmount, validationUnit))
                .build();
        return couponRepository.save(coupon).getId();
    }

    @Transactional
    public CouponInfo acquireCouponPessimistic(Long userId, Long couponId) {
        Coupon coupon = couponRepository.findByIdForUpdate(couponId)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));

        coupon.acquire();
        userCouponRepository.save(UserCoupon.builder()
                .userId(userId)
                .coupon(coupon)
                .validation(Duration.ofMinutes(coupon.getValidationInMinutes()))
                .build());
        return CouponInfo.builder().coupon(coupon).build();
    }

    @Transactional
    public CouponInfo acquireCoupon(Long userId, Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));

        coupon.acquire();
        userCouponRepository.save(UserCoupon.builder()
                .userId(userId)
                .coupon(coupon)
                .validation(Duration.ofMinutes(coupon.getValidationInMinutes()))
                .build());
        return CouponInfo.builder().coupon(coupon).build();
    }

    @Transactional
    public void useCoupon(Long userCouponId, Long itemId, Long orderId) {
        UserCoupon userCoupon = userCouponRepository.findById(userCouponId)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));
        userCoupon.use(itemId, orderId);
    }
}
