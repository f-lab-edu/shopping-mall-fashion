package com.example.flab.soft.shoppingmallfashion.coupon.domain;

import com.example.flab.soft.shoppingmallfashion.common.BaseEntity;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.Duration;
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
    private Long amounts;
    @Embedded
    private Discount discount;
    private Long validationInMinutes;

    @Builder
    public Coupon(String name, Long amounts, Discount discount, Duration validation) {
        this.name = name;
        this.amounts = amounts;
        this.discount = discount;
        this.validationInMinutes = validation.toMinutes();
    }

    public void acquire() {
        if (amounts == 0) {
            throw new ApiException(ErrorEnum.OUT_OF_COUPON);
        }
        amounts--;
    }
}
