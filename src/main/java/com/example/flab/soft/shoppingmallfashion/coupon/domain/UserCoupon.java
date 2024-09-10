package com.example.flab.soft.shoppingmallfashion.coupon.domain;

import com.example.flab.soft.shoppingmallfashion.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "user_coupons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserCoupon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long userId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;
    @Embedded
    private UsageInfo usageInfo = UsageInfo.builder().used(false).build();
    private LocalDateTime expiredAt;

    @Builder
    public UserCoupon(Long userId, Coupon coupon, TemporalAmount validation) {
        this.userId = userId;
        this.coupon = coupon;
        this.expiredAt = LocalDateTime.now().plus(validation);
    }

    public void use(Long itemId, Long orderId) {
        usageInfo.use(itemId, orderId);
    }

    public Boolean isUsed() {
        return usageInfo.isUsed();
    }

    public Boolean isNotExpired() {
        return LocalDateTime.now().isBefore(expiredAt);
    }

    public String getCouponName() {
        return coupon.getName();
    }
}
