package com.example.flab.soft.shoppingmallfashion.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatedDataInfo {
    private Long createdCount;
    private Long firstElementId;
}
