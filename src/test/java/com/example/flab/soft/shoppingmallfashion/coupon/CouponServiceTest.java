package com.example.flab.soft.shoppingmallfashion.coupon;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
class CouponServiceTest {
    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponRedissonService couponRedissonService;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private CouponRedissonRepository couponRedissonRepository;
    private static final int THREAD_NUMBER = 100;
    private ExecutorService threads = Executors.newFixedThreadPool(THREAD_NUMBER);
    private CountDownLatch latch = new CountDownLatch(THREAD_NUMBER);
    private static final int ISSUED_AMOUNT = 100;
    @Autowired
    private PlatformTransactionManager txManager;

    @AfterEach
    void tearDown() {
        couponRepository.deleteAll();
        couponRedissonRepository.deleteAll();
    }

    @Test
    @DisplayName("쿠폰 100개 발행")
    @Transactional
    void issueCoupons() {
        Integer issuedCouponsCount = couponService.issueCoupons("welcomeCoupon",
                DiscountType.FIXED_PRICE_DISCOUNT,
                1000, DiscountUnit.KRW, ISSUED_AMOUNT);

        assertThat(issuedCouponsCount).isEqualTo(ISSUED_AMOUNT);
        assertThat(couponRepository.count()).isEqualTo(ISSUED_AMOUNT);
    }

    @Test
    @DisplayName("쿠폰 제공")
    @Transactional
    void getCoupons() {
        Long userId = 1L;
        Coupon coupon = couponRepository.save(Coupon.builder()
                .name("welcomeCoupon")
                .discount(Discount.builder()
                        .discountType(DiscountType.FIXED_PRICE_DISCOUNT)
                        .amount(1000)
                        .discountUnit(DiscountUnit.KRW)
                        .build())
                .expiration(LocalDateTime.now().plusMonths(3))
                .build());

        CouponInfo couponInfo = couponService.getCoupon(userId, "welcomeCoupon");
        assertThat(coupon.getOwnerId()).isEqualTo(userId);
        assertThat(coupon.getOwned()).isTrue();
        assertThat(coupon.getName()).isEqualTo(couponInfo.getCouponName());
    }

    @Test
    @DisplayName("쿠폰 제공 동시성 테스트")
    void couponRaceConditionTest() throws InterruptedException {
        TransactionTemplate txTemplate = new TransactionTemplate(txManager);
        couponService.issueCoupons("welcomeCoupon",
                DiscountType.FIXED_PRICE_DISCOUNT,
                1000, DiscountUnit.KRW, THREAD_NUMBER);
        long startAt = System.currentTimeMillis();
        for (int i = 1; i < THREAD_NUMBER + 1; i++) {
            long finalI = i;
            threads.execute(() -> {
                try {
                    couponService.getCoupon(finalI, "welcomeCoupon");
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        System.out.println("timeCost = " + (System.currentTimeMillis() - startAt));
        List<Coupon> coupons = couponRedissonRepository.findAll();
        assertThat(coupons).allMatch(Coupon::hasOwner);
    }

    @Test
    @DisplayName("Redisson 이용한 쿠폰 제공 동시성 테스트")
    void couponRaceConditionRedissonTest() throws InterruptedException {
        TransactionTemplate txTemplate = new TransactionTemplate(txManager);
        couponService.issueCoupons("welcomeCoupon",
                DiscountType.FIXED_PRICE_DISCOUNT,
                1000, DiscountUnit.KRW, THREAD_NUMBER);
        long startAt = System.currentTimeMillis();
        for (int i = 1; i < THREAD_NUMBER + 1; i++) {
            long finalI = i;
            threads.execute(() -> {
                try {
                    couponRedissonService.getCoupon(finalI, "welcomeCoupon");
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        System.out.println("timeCost = " + (System.currentTimeMillis() - startAt));
        List<Coupon> coupons = couponRedissonRepository.findAll();
        assertThat(coupons).allMatch(Coupon::hasOwner);
    }
}