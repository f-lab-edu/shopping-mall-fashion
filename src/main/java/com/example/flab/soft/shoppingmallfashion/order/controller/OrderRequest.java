package com.example.flab.soft.shoppingmallfashion.order.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    @NotNull
    private Long itemOptionId;
    @NotNull
    @Min(1)
    @Max(10)
    private Integer orderAmount;
    @NotNull
    @Min(0)
    @Max(10000000)
    private Integer totalPrice;
    @NotNull
    @Min(0)
    @Max(1000000)
    private Integer discountedAmount;
    @NotNull
    @Min(0)
    @Max(100000000)
    private Integer paymentAmount;
    @NotBlank
    private String recipientName;
    @NotBlank
    private String roadAddress;
    @NotBlank
    private String addressDetail;
}
