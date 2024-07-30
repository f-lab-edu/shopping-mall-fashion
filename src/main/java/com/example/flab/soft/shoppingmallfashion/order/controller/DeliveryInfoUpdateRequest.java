package com.example.flab.soft.shoppingmallfashion.order.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryInfoUpdateRequest {
    @NotBlank
    private String recipientName;
    @NotBlank
    private String roadAddress;
    @NotBlank
    private String addressDetail;
}
