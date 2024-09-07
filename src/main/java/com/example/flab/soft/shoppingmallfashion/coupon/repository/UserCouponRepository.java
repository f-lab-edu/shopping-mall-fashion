package com.example.flab.soft.shoppingmallfashion.coupon.repository;

import com.example.flab.soft.shoppingmallfashion.coupon.domain.UserCoupon;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    List<UserCoupon> findByUserId(Long userId);

    List<UserCoupon> findAllByCouponId(Long couponId);
}
