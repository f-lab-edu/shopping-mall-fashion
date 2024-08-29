package com.example.flab.soft.shoppingmallfashion.store.controller;

import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetails.AuthUser;
import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import com.example.flab.soft.shoppingmallfashion.store.service.NewStoreRegisterService;
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
    private final NewStoreRegisterService newStoreRegisterService;

    @PostMapping("/register")
    public SuccessResult<Void> registerStore(
            @Validated @RequestBody StoreRegisterRequest storeRegisterRequest) {
        newStoreRegisterService.registerStore(storeRegisterRequest);
        return SuccessResult.<Void>builder().build();
    }

    @GetMapping("/my-store")
    public SuccessResult<StoreDto> myStore(
            @AuthenticationPrincipal AuthUser user) {
        StoreDto storeDto = storeService.getUserStore(user.getId());
        return SuccessResult.<StoreDto>builder().response(storeDto).build();
    }

    @PatchMapping("/my-store")
    public SuccessResult<StoreDto> patchStoreInfo(
            @AuthenticationPrincipal AuthUser user,
            @RequestBody StoreInfoPatchRequest storeInfoPatchRequest) {
        StoreDto storeDto = storeService.updateMyStore(
                storeInfoPatchRequest.buildStoreUpdateDto(), user.getId());
        return SuccessResult.<StoreDto>builder().response(storeDto).build();
    }

    @PatchMapping("/my-store/stoppage")
    public SuccessResult<StoreDto> stoppage(
            @AuthenticationPrincipal AuthUser user) {
        StoreDto storeDto = storeService.stoppage(user.getId());
        return SuccessResult.<StoreDto>builder().response(storeDto).build();
    }
}
