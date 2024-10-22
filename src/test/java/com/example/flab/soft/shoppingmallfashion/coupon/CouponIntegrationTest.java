package com.example.flab.soft.shoppingmallfashion.coupon;

import static org.assertj.core.api.Assertions.*;

import com.example.flab.soft.shoppingmallfashion.coupon.domain.Coupon;
import com.example.flab.soft.shoppingmallfashion.coupon.domain.Discount;
import com.example.flab.soft.shoppingmallfashion.coupon.domain.DiscountType;
import com.example.flab.soft.shoppingmallfashion.coupon.domain.UserCoupon;
import com.example.flab.soft.shoppingmallfashion.coupon.repository.CouponRepository;
import com.example.flab.soft.shoppingmallfashion.coupon.repository.UserCouponRepository;
import com.example.flab.soft.shoppingmallfashion.coupon.service.CouponInfo;
import com.example.flab.soft.shoppingmallfashion.coupon.service.CouponRedissonService;
import com.example.flab.soft.shoppingmallfashion.coupon.service.CouponService;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class CouponIntegrationTest {
    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponRedissonService couponRedissonService;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private UserCouponRepository userCouponRepository;
    @Autowired
    private static final int THREAD_NUMBER = 100;
    private ExecutorService threads = Executors.newFixedThreadPool(THREAD_NUMBER);
    private CountDownLatch latch = new CountDownLatch(THREAD_NUMBER);
    private static final long ISSUED_AMOUNT = 10;

    @AfterEach
    void tearDown() {
        userCouponRepository.deleteAll();
        couponRepository.deleteAll();
    }

    @Test
    @DisplayName("쿠폰 100개 발행")
    @Transactional
    void issueCoupons() {
        Long couponId = couponService.issueCoupons("welcomeCoupon",
                DiscountType.FIXED_DISCOUNT_KRW,
                1000, ISSUED_AMOUNT,
                3L, ChronoUnit.DAYS);

        Coupon coupon = couponRepository.findById(couponId).get();
        assertThat(coupon.getAmounts()).isEqualTo(ISSUED_AMOUNT);
        assertThat(couponRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("쿠폰 제공")
    @Transactional
    void getCoupons() {
        Long userId = 1L;
        Coupon coupon = couponRepository.save(Coupon.builder()
                .name("welcomeCoupon")
                .amounts(1L)
                .discount(Discount.builder()
                        .discountType(DiscountType.FIXED_DISCOUNT_KRW)
                        .discountAmount(1000)
                        .build())
                .validation(Duration.of(3, ChronoUnit.DAYS))
                .build());

        CouponInfo couponInfo = couponService.acquireCoupon(userId, coupon.getId());
        List<UserCoupon> userCoupons = userCouponRepository.findByUserId(userId);
        assertThat(userCoupons.size()).isEqualTo(1);
        assertThat(userCoupons.get(0).getCoupon()).isEqualTo(coupon);
        assertThat(coupon.getName()).isEqualTo(couponInfo.getCouponName());
    }

    @Test
    @DisplayName("쿠폰 제공 동시성 테스트")
    void couponRaceConditionTest() throws InterruptedException {
        Coupon coupon = couponRepository.save(Coupon.builder()
                .name("welcomeCoupon")
                .amounts((long) THREAD_NUMBER)
                .discount(Discount.builder()
                        .discountType(DiscountType.FIXED_DISCOUNT_KRW)
                        .discountAmount(1000)
                        .build())
                .validation(Duration.of(3, ChronoUnit.DAYS))
                .build());
        long startAt = System.currentTimeMillis();
        for (int i = 1; i < THREAD_NUMBER + 1; i++) {
            long userId = i % 9 + 1;
            threads.execute(() -> {
                try {
                    couponService.acquireCouponPessimistic(userId, coupon.getId());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        System.out.println("timeCost = " + (System.currentTimeMillis() - startAt));
        assertThat(userCouponRepository.count()).isEqualTo(THREAD_NUMBER);
        assertThat(couponRepository.findById(coupon.getId()).get().getAmounts()).isEqualTo(0);
    }

    @Test
    @DisplayName("Redisson 이용한 쿠폰 제공 동시성 테스트")
    void couponRaceConditionRedissonTest() throws InterruptedException {
        Coupon coupon = couponRepository.save(Coupon.builder()
                .name("welcomeCoupon")
                .amounts((long) THREAD_NUMBER)
                .discount(Discount.builder()
                        .discountType(DiscountType.FIXED_DISCOUNT_KRW)
                        .discountAmount(1000)
                        .build())
                .validation(Duration.of(3, ChronoUnit.DAYS))
                .build());
        long startAt = System.currentTimeMillis();
        for (int i = 1; i < THREAD_NUMBER + 1; i++) {
            long userId = i % 9 + 1;
            threads.execute(() -> {
                try {
                    couponRedissonService.acquireCoupon(userId, coupon.getId());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        System.out.println("timeCost = " + (System.currentTimeMillis() - startAt));
        assertThat(userCouponRepository.count()).isEqualTo(THREAD_NUMBER);
        assertThat(couponRepository.findById(coupon.getId()).get().getAmounts()).isEqualTo(0);
    }

    @Test
    @DisplayName("쿠폰 사용, 유효기간이 가장 적게 남은 쿠폰 먼저 사용")
    void useCoupon() {
        //given
        Coupon coupon = couponRepository.save(Coupon.builder()
                .name("welcomeCoupon")
                .amounts(1L)
                .discount(Discount.builder()
                        .discountType(DiscountType.FIXED_DISCOUNT_KRW)
                        .discountAmount(1000)
                        .build())
                .validation(Duration.of(3, ChronoUnit.DAYS))
                .build());
        Long orderId = 1L;
        Long itemId = 2L;
        Long userId = 1L;

        UserCoupon userCouponToBeExpiredFirst = userCouponRepository.save(UserCoupon.builder()
                .coupon(coupon)
                .userId(userId)
                .validation(Duration.ofDays(1))
                .build());

        UserCoupon userCoupon = userCouponRepository.save(UserCoupon.builder()
                .coupon(coupon)
                .userId(userId)
                .validation(Duration.ofDays(3))
                .build());
        //when
        couponService.useCoupon(userCoupon.getCoupon().getId(), userId, itemId, orderId);
        //then
        UserCoupon usedUserCoupon = userCouponRepository.findById(userCouponToBeExpiredFirst.getId()).get();
        UserCoupon notUsedUserCoupon = userCouponRepository.findById(userCoupon.getId()).get();
        assertThat(usedUserCoupon.isUsed()).isTrue();
        assertThat(usedUserCoupon.getUsageInfo().getUsed()).isTrue();
        assertThat(usedUserCoupon.getUsageInfo().getUsedOrderId()).isEqualTo(orderId);
        assertThat(usedUserCoupon.getUsageInfo().getUsedItemId()).isEqualTo(itemId);

        assertThat(notUsedUserCoupon.isUsed()).isFalse();
    }
}