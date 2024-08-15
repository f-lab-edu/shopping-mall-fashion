package com.example.flab.soft.shoppingmallfashion.coupon.service;

import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CouponRedissonService{
    private final CouponService couponService;
    private final RedissonClient redissonClient;

    public CouponInfo acquireCoupon(Long userId, Long couponId) {
        RLock lock = redissonClient.getLock(String.valueOf(couponId));
        try {
            boolean acquireLock = lock.tryLock(30, 2, TimeUnit.SECONDS);
            if (!acquireLock) {
                throw new ApiException(ErrorEnum.RETRY_GET_COUPON);
            }
            log.debug("Lock acquired by user: {}", userId);
            return couponService.acquireCoupon(userId, couponId);
        } catch (InterruptedException e) {
            throw new ApiException(ErrorEnum.RETRY_GET_COUPON);
        } finally {
            lock.unlock();
            log.debug("Lock released by user: {}", userId);
        }
    }
}
