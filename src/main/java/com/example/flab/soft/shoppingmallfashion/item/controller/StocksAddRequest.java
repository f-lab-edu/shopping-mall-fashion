package com.example.flab.soft.shoppingmallfashion.item.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StocksAddRequest {
    private Integer amount;
}
