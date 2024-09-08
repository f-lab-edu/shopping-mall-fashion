package com.example.flab.soft.shoppingmallfashion.coupon.service;

import com.example.flab.soft.shoppingmallfashion.coupon.domain.Discount;
import com.example.flab.soft.shoppingmallfashion.coupon.repository.CouponRepository;
import com.example.flab.soft.shoppingmallfashion.coupon.repository.UserCouponRepository;
import com.example.flab.soft.shoppingmallfashion.coupon.domain.Coupon;
import com.example.flab.soft.shoppingmallfashion.coupon.domain.DiscountType;
import com.example.flab.soft.shoppingmallfashion.coupon.domain.UserCoupon;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.order.domain.UsedCouponInfo;
import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
                             Long issuedAmount, Long validationAmount,
                             TemporalUnit validationUnit) {
        Coupon coupon = Coupon.builder()
                .name(couponName)
                .amounts(issuedAmount)
                .discount(Discount.builder()
                        .discountType(discountType)
                        .discountAmount(discountedAmount)
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
    public UsedCouponInfo useCoupon(Long couponId, Long userId, Long itemId, Long orderId) {
        List<UserCoupon> coupons = userCouponRepository.findAllByUserIdAndCouponId(userId, couponId);

        if (coupons.isEmpty()) {
            throw new ApiException(ErrorEnum.INVALID_REQUEST);
        }
        UserCoupon couponToUse = coupons.stream().min(Comparator.comparing(UserCoupon::getExpiredAt)).get();
        couponToUse.use(itemId, orderId);

        return UsedCouponInfo.builder()
                .usedUserCouponId(couponToUse.getId())
                .usedCouponName(couponToUse.getCouponName())
                .build();
    }

    @Transactional
    public List<UserCouponInfo> findAllCouponsByUserId(Long userId) {
        List<UserCoupon> userCoupons = userCouponRepository.findByUserId(userId);

        Map<Coupon, List<UserCoupon>> couponUserCouponMap = userCoupons.stream()
                .filter(UserCoupon::isNotExpired)
                .collect(Collectors.groupingBy(UserCoupon::getCoupon));

        return couponUserCouponMap.entrySet().stream()
                .map(entry -> UserCouponInfo.builder()
                        .couponInfo(CouponInfo.builder()
                                .coupon(entry.getKey())
                                .build())
                        .quantity(entry.getValue().size())
                        .expiredAt(entry.getValue().stream()
                                .map(UserCoupon::getExpiredAt)
                                .sorted()
                                .toList())
                        .build())
                .toList();
    }
}
