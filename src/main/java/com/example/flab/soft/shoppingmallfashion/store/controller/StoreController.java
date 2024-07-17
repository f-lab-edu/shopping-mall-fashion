package com.example.flab.soft.shoppingmallfashion.store.controller;

import com.example.flab.soft.shoppingmallfashion.auth.AuthUser;
import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import com.example.flab.soft.shoppingmallfashion.store.service.StoreDto;
import com.example.flab.soft.shoppingmallfashion.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store")
public class StoreController {
    private final StoreService storeService;

    @PostMapping("/register")
    public SuccessResult<Void> registerStore(
            @AuthenticationPrincipal AuthUser user,
            @Validated @RequestBody AddStoreRequest addStoreRequest) {
        storeService.registerStore(addStoreRequest, user.getId());
        return SuccessResult.<Void>builder().build();
    }

    @GetMapping("/my-store")
    public SuccessResult<StoreDto> myStore(
            @AuthenticationPrincipal AuthUser user) {
        StoreDto storeDto = storeService.getUserStore(user.getId());
        return SuccessResult.<StoreDto>builder().response(storeDto).build();
    }

    @PatchMapping("/my-store")
    public SuccessResult<StoreDto> updateMyStore(
            @AuthenticationPrincipal AuthUser user,
            @RequestBody StoreFieldUpdateRequest storeFieldUpdateRequest) {
        StoreDto storeDto = storeService.updateMyStore(
                storeFieldUpdateRequest.buildStoreUpdateDto(), user.getId());
        return SuccessResult.<StoreDto>builder().response(storeDto).build();
    }

    @PatchMapping("/my-store/stoppage")
    public SuccessResult<StoreDto> stoppage(
            @AuthenticationPrincipal AuthUser user) {
        StoreDto storeDto = storeService.stoppage(user.getId());
        return SuccessResult.<StoreDto>builder().response(storeDto).build();
    }
}
