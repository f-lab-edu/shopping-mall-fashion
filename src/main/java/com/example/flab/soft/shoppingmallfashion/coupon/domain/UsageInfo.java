package com.example.flab.soft.shoppingmallfashion.coupon.domain;

import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsageInfo {
    private Boolean used;
    private Long usedItemId;
    private Long usedOrderId;

    public Boolean isUsed() {
        return used;
    }

    public void use(Long itemId, Long orderId) {
        if (used) {
            throw new ApiException(ErrorEnum.USED_COUPON);
        }
        this.used = true;
        this.usedItemId = itemId;
        this.usedOrderId = orderId;
    }
}
