package com.example.flab.soft.shoppingmallfashion.item.domain;

import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ItemStats {
    private Long orderCount = 0L;
    private Long viewCount = 0L;
    private Integer reviewCount = 0;
    private Integer reviewGrade = 0;
    private Integer saleGrade = 0;

    public void modifyOrderCount(Long orderCount) {
        if (orderCount < 0) {
            throw new ApiException(ErrorEnum.INVALID_REQUEST);
        }
        this.orderCount = orderCount;
    }
}
