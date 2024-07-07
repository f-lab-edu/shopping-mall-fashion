package com.example.flab.soft.shoppingmallfashion.address.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AddressAddRequest {
    @NotBlank
    private String recipientName;
    @NotBlank
    private String roadAddress;
    @NotBlank
    private String addressDetail;
    @Min(value = 10000)
    @Max(value = 99999)
    private int zipcode;
    @Pattern(regexp = "^010[0-9]{8}$")
    private String recipientCellphone;

    @Builder
    public AddressAddRequest(String recipientName, String roadAddress, String addressDetail, int zipcode,
                             String recipientCellphone) {
        this.recipientName = recipientName;
        this.roadAddress = roadAddress;
        this.addressDetail = addressDetail;
        this.zipcode = zipcode;
        this.recipientCellphone = recipientCellphone;
    }
}
