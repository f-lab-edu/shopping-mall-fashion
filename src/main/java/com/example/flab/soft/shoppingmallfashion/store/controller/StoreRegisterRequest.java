package com.example.flab.soft.shoppingmallfashion.store.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreRegisterRequest {
    @NotBlank
    private String requesterName;
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$")
    private String requesterEmail;
    @Pattern(regexp = "^010[0-9]{8}$")
    private String requesterPhoneNumber;
    @Pattern(regexp = "\\d{10}")
    private String businessRegistrationNumber;
}
