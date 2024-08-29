package com.example.flab.soft.shoppingmallfashion.coupon;

import static org.mockito.Mockito.mock;

import com.example.flab.soft.shoppingmallfashion.coupon.domain.Coupon;
import com.example.flab.soft.shoppingmallfashion.coupon.domain.UserCoupon;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import java.time.Duration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserCouponTest {
    @Test
    @DisplayName("이미 사용된 쿠폰을 사용시에 예외")
    void cannotUseUsedCoupon() {
        //given
        Coupon coupon = mock(Coupon.class);
        UserCoupon userCoupon = UserCoupon.builder()
                .coupon(coupon)
                .userId(1L)
                .validation(Duration.ofDays(3))
                .build();
        userCoupon.use(1L, 1L);
        //when, then
        Assertions.assertThatThrownBy(() -> userCoupon.use(1L, 1L))
                .hasMessage(ErrorEnum.USED_COUPON.getMessage());
    }
}
