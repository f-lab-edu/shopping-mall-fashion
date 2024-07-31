package com.example.flab.soft.shoppingmallfashion.coupon;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM coupons c WHERE c.name = :couponName AND c.owned = FALSE")
    List<Coupon> findFirstUnownedCouponByName(String couponName, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Coupon> findFirstByNameAndOwnedIsFalse(String couponName);
}
