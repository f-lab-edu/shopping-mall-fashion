package com.example.flab.soft.shoppingmallfashion.store.controller;

import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetails.AuthCrew;
import com.example.flab.soft.shoppingmallfashion.auth.role.Role;
import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.store.service.CrewBriefInfo;
import com.example.flab.soft.shoppingmallfashion.store.service.CrewService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/{storeId}/crews")
public class CrewController {
    private final CrewService crewService;

    @PostMapping("/signup")
    public SuccessResult<CrewBriefInfo> signUp(
            @PathVariable @NotNull Long storeId,
            @Validated @RequestBody CrewSignUpRequest crewSignUpRequest) {
        return SuccessResult.<CrewBriefInfo>builder()
                .response(crewService.addCrew(crewSignUpRequest, storeId))
                .build();
    }

    @PatchMapping("/{crewId}/approval")
    public SuccessResult<CrewBriefInfo> approve(
            @AuthenticationPrincipal AuthCrew crewManager,
            @PathVariable @NotNull Long storeId,
            @NotNull @PathVariable Long crewId) {
        if (!crewManager.getStoreId().equals(storeId)) {
            throw new ApiException(ErrorEnum.FORBIDDEN);
        }
        return SuccessResult.<CrewBriefInfo>builder()
                .response(crewService.approve(crewId))
                .build();
    }

    @PatchMapping("/{crewId}/roles")
    public SuccessResult<CrewBriefInfo> updateRoles(
            @AuthenticationPrincipal AuthCrew crewManager,
            @PathVariable @NotNull Long storeId,
            @NotNull @PathVariable Long crewId,
            @RequestBody List<Role> roles) {
        if (!crewManager.getStoreId().equals(storeId)) {
            throw new ApiException(ErrorEnum.FORBIDDEN);
        }
        return SuccessResult.<CrewBriefInfo>builder()
                .response(crewService.updateRoles(crewId, roles))
                .build();
    }
}
