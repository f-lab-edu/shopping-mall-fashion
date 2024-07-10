package com.example.flab.soft.shoppingmallfashion.store.controller;

import com.example.flab.soft.shoppingmallfashion.auth.AuthUser;
import com.example.flab.soft.shoppingmallfashion.auth.role.Authority;
import com.example.flab.soft.shoppingmallfashion.auth.role.RoleService;
import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import com.example.flab.soft.shoppingmallfashion.store.service.StoreDto;
import com.example.flab.soft.shoppingmallfashion.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
            @RequestBody AddStoreRequest addStoreRequest) {
        storeService.addStore(addStoreRequest, user.getId());
        roleService.save(user.getId(), Authority.ROLE_STORE_MANAGER);
        return SuccessResult.<Void>builder().build();
    }

    @GetMapping("/myStore")
    public SuccessResult<StoreDto> myStore(
            @AuthenticationPrincipal AuthUser user) {
        StoreDto storeDto = storeService.getUserStore(user.getId());
        return SuccessResult.<StoreDto>builder().response(storeDto).build();
    }
}
