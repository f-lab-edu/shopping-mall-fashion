package com.example.flab.soft.shoppingmallfashion.coupon;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface CouponRedissonRepository extends JpaRepository<Coupon, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Coupon> findFirstByNameAndOwnedIsFalse(String couponName);
}
