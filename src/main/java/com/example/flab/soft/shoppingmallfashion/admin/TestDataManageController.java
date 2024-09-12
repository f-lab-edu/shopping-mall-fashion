package com.example.flab.soft.shoppingmallfashion.admin;

import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/test-data")
public class TestDataManageController {
    private final TestDataManageService testDataManageService;

    @PostMapping
    public SuccessResult<CurrentTestDataCountsDto> initTestData(@RequestBody TestDataCountRequirements testDataCountRequirements) {
        return SuccessResult.<CurrentTestDataCountsDto>builder().response(testDataManageService.init(testDataCountRequirements)).build();
    }

    @DeleteMapping
    public SuccessResult<CurrentTestDataCountsDto> clearTestData() {
        return SuccessResult.<CurrentTestDataCountsDto>builder().response(testDataManageService.clearAll()).build();
    }
}
