package com.example.flab.soft.shoppingmallfashion.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCountsDto {
    private Long itemCount;
    private Long itemOptionCount;
}
