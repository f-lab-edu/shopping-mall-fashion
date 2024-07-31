package com.example.flab.soft.shoppingmallfashion.coupon;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.LongStream;
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
    private CouponRepository couponRepository;
    private ExecutorService threads = Executors.newFixedThreadPool(10);
    private static final int ISSUED_AMOUNT = 100;
    @Autowired
    private PlatformTransactionManager txManager;

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
    void couponRaceConditionTest() {
        TransactionTemplate txTemplate = new TransactionTemplate(txManager);
        couponService.issueCoupons("welcomeCoupon",
                DiscountType.FIXED_PRICE_DISCOUNT,
                1000, DiscountUnit.KRW, 10);
        List<Future<CouponInfo>> jobs = LongStream.range(1, 11)
                .mapToObj(i -> threads.submit(() ->
                    txTemplate.execute(status ->
                            couponService.getCoupon(i, "welcomeCoupon"))))
                .toList();

        jobs.forEach(it -> {
            try {
                it.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }});
        List<Coupon> coupons = couponRepository.findAll();
        assertThat(coupons).allMatch(Coupon::hasOwner);
    }
}