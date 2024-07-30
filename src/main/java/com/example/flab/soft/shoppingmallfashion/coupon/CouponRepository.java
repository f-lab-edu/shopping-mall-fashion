package com.example.flab.soft.shoppingmallfashion.coupon;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM coupons c WHERE c.name = :couponName AND c.usageInfo.used = FALSE")
    Optional<Coupon> findFirstUnusedCouponByName(String couponName, Limit limit);
}
