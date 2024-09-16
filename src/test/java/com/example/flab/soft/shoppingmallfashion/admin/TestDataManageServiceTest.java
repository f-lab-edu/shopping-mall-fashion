package com.example.flab.soft.shoppingmallfashion.admin;

import static org.assertj.core.api.Assertions.*;

import com.example.flab.soft.shoppingmallfashion.admin.dto.CreatedDataInfo;
import com.example.flab.soft.shoppingmallfashion.admin.service.UserTestDataManageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestDataManageServiceTest {
    @Autowired
    private UserTestDataManageService userTestDataManageService;

    @Test
    void createTestUsers() {
        Integer userCount = 10;
        CreatedDataInfo createdDataInfo = userTestDataManageService.createTestUsers(userCount);
        assertThat(createdDataInfo.getCreatedCount()).isEqualTo((long) userCount + 10);
    }
}