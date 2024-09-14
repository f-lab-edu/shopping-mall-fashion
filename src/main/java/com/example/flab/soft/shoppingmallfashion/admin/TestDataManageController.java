package com.example.flab.soft.shoppingmallfashion.admin;

import com.example.flab.soft.shoppingmallfashion.admin.dto.CurrentTestDataCountsDto;
import com.example.flab.soft.shoppingmallfashion.admin.dto.TestDataCountRequirements;
import com.example.flab.soft.shoppingmallfashion.admin.service.TestDataManageService;
import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/test-data")
public class TestDataManageController {
    private final TestDataManageService testDataManageService;

    @GetMapping
    public SuccessResult<CurrentTestDataCountsDto> countTestData() {
        return SuccessResult.<CurrentTestDataCountsDto>builder().response(testDataManageService.count()).build();
    }

    @PostMapping
    public SuccessResult<Void> initTestData(@RequestBody TestDataCountRequirements testDataCountRequirements) {
        testDataManageService.init(testDataCountRequirements);
        return SuccessResult.<Void>builder().build();
    }

    @DeleteMapping
    public SuccessResult<Void> clearTestData() {
        testDataManageService.clearAll();
        return SuccessResult.<Void>builder().build();
    }
}
