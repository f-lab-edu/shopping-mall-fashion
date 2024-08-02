package com.example.flab.soft.shoppingmallfashion.coupon;

import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@Slf4j
public class CouponRedissonService{
    private final CouponRedissonRepository couponRedissonRepository;
    private final RedissonClient redissonClient;
    private final TransactionTemplate txTemplate;

    public CouponRedissonService(CouponRedissonRepository couponRedissonRepository, RedissonClient redissonClient,
                                 PlatformTransactionManager txManager) {
        this.couponRedissonRepository = couponRedissonRepository;
        this.redissonClient = redissonClient;
        this.txTemplate = new TransactionTemplate(txManager);
    }

    public CouponInfo getCoupon(Long userId, String couponName) {
        RLock lock = redissonClient.getLock(couponName);

        try {
            boolean acquireLock = lock.tryLock(10, 1, TimeUnit.SECONDS);
            if (!acquireLock) {
                throw new ApiException(ErrorEnum.RETRY_GET_COUPON);
            }
            log.debug("Lock acquired by user: {}", userId);
            return txTemplate.execute(status -> {
                Coupon coupon = couponRedissonRepository.findFirstByNameAndOwnedIsFalse(couponName)
                        .orElseThrow(() -> new ApiException(ErrorEnum.OUT_OF_COUPON));
                coupon.decideOwner(userId);
                log.debug("user {} got coupon {}, coupon ownerId = {}",userId, coupon.getId(), coupon.getOwnerId());
                return CouponInfo.builder().coupon(coupon).build();
            });
        } catch (InterruptedException e) {
            throw new ApiException(ErrorEnum.RETRY_GET_COUPON);
        } finally {
            lock.unlock();
            log.debug("Lock released by user: {}", userId);
        }
    }
}
