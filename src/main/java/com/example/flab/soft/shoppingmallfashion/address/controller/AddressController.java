package com.example.flab.soft.shoppingmallfashion.address.controller;

import com.example.flab.soft.shoppingmallfashion.address.service.AddressService;
import com.example.flab.soft.shoppingmallfashion.address.service.Addresses;
import com.example.flab.soft.shoppingmallfashion.auth.AuthUser;
import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;
    @PostMapping
    public SuccessResult<Void> addAddress(@RequestBody @Validated AddressAddRequest request, @AuthenticationPrincipal AuthUser user){
        addressService.add(request, user.getId());
        return SuccessResult.<Void>builder().build();
    }

    @DeleteMapping("/{id}")
    public SuccessResult<Void> deleteAddress(@PathVariable Long id, @AuthenticationPrincipal AuthUser user){
        addressService.delete(id, user.getId());
        return SuccessResult.<Void>builder().build();
    }

    @GetMapping()
    public SuccessResult<Addresses> myAddresses(@AuthenticationPrincipal AuthUser user){
        Addresses addresses = addressService.getAddresses(user.getId());
        return SuccessResult.<Addresses>builder().response(addresses).build();
    }
}
