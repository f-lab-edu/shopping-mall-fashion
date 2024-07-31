package com.example.flab.soft.shoppingmallfashion.coupon;

import com.example.flab.soft.shoppingmallfashion.common.BaseEntity;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "coupons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Coupon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private Long ownerId;
    private Boolean owned = false;
    @Embedded
    private UsageInfo usageInfo = UsageInfo.builder().used(false).build();
    @Embedded
    private Discount discount;
    private LocalDateTime expiration;

    @Builder
    public Coupon(String name, Discount discount, LocalDateTime expiration) {
        this.name = name;
        this.discount = discount;
        this.expiration = expiration;
    }

    public void decideOwner(Long userId) {
        if (owned) {
            throw new ApiException(ErrorEnum.ALREADY_OWNED_COUPON);
        }
        ownerId = userId;
        owned = true;
    }

    public boolean hasOwner() {
        return ownerId != null;
    }
}
