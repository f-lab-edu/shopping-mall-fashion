package com.example.flab.soft.shoppingmallfashion.store.controller;

import com.example.flab.soft.shoppingmallfashion.auth.AuthUser;
import com.example.flab.soft.shoppingmallfashion.auth.role.Authority;
import com.example.flab.soft.shoppingmallfashion.auth.role.RoleService;
import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import com.example.flab.soft.shoppingmallfashion.store.service.StoreDto;
import com.example.flab.soft.shoppingmallfashion.store.service.StoreService;
import com.example.flab.soft.shoppingmallfashion.store.service.StoreUpdateDto;
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
    private final RoleService roleService;

    @PostMapping("/register")
    public SuccessResult<Void> registerStore(
            @AuthenticationPrincipal AuthUser user,
            @Validated @RequestBody AddStoreRequest addStoreRequest) {
        storeService.addStore(addStoreRequest, user.getId());
        roleService.save(user.getId(), Authority.ROLE_STORE_MANAGER);
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
            @Validated @RequestBody StoreFieldUpdateRequest storeFieldUpdateRequest) {
        StoreDto storeDto = storeService.updateMyStore(
                buildStoreUpdateDto(storeFieldUpdateRequest), user.getId());
        return SuccessResult.<StoreDto>builder().response(storeDto).build();
    }

    private static StoreUpdateDto buildStoreUpdateDto(StoreFieldUpdateRequest storeFieldUpdateRequest) {
        return StoreUpdateDto.builder().name(storeFieldUpdateRequest.getName()).logo(storeFieldUpdateRequest.getLogo())
                .description(storeFieldUpdateRequest.getDescription()).build();
    }

    @PatchMapping("/my-store/stoppage")
    public SuccessResult<StoreDto> stoppage(
            @AuthenticationPrincipal AuthUser user) {
        StoreDto storeDto = storeService.stoppage(user.getId());
        return SuccessResult.<StoreDto>builder().response(storeDto).build();
    }
}
