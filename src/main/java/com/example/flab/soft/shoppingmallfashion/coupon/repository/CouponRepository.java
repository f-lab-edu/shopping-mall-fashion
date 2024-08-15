package com.example.flab.soft.shoppingmallfashion.coupon.repository;

import com.example.flab.soft.shoppingmallfashion.coupon.domain.Coupon;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM coupons c WHERE c.id = :couponId")
    Optional<Coupon> findByIdForUpdate(Long couponId);
}
