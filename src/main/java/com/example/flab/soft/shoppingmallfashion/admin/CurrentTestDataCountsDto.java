package com.example.flab.soft.shoppingmallfashion.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrentTestDataCountsDto {
    private Long currentUsersCount;
    private Long currentStoresCount;
    private Long currentCategoriesCount;
    private Long currentItemsCount;
    private Long currentItemOptionsCount;
}
