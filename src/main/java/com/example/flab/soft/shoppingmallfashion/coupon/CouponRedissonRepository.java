package com.example.flab.soft.shoppingmallfashion.coupon;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRedissonRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findFirstByNameAndOwnedIsFalse(String couponName);
}
