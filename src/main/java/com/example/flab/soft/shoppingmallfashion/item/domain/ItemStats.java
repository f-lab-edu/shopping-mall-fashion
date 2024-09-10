package com.example.flab.soft.shoppingmallfashion.item.domain;

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
}
