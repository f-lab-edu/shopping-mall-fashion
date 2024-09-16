package com.example.flab.soft.shoppingmallfashion.admin.service;

import com.example.flab.soft.shoppingmallfashion.admin.dto.TestDataCountRequirements;
import com.example.flab.soft.shoppingmallfashion.admin.dto.CreatedDataInfo;
import com.example.flab.soft.shoppingmallfashion.admin.dto.CurrentTestDataCountsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestDataManageService {
    private final UserTestDataManageService userTestDataManageService;
    private final StoreTestDataManageService storeTestDataManageService;
    private final CategoryManageService categoryManageService;
    private final ItemTestDataManageService itemTestDataManageService;

    @Async
    public void init(TestDataCountRequirements testDataCountRequirements) {
        long before = System.currentTimeMillis();
        CreatedDataInfo createdCategoryDataInfo = categoryManageService.initCategory();
        log.info("카테고리 초기화에 걸린 시간: {}", System.currentTimeMillis() - before);
        before = System.currentTimeMillis();
        CreatedDataInfo createdUserDataInfo = userTestDataManageService.createTestUsers(testDataCountRequirements.getUserCount());
        userTestDataManageService.encryptPassword();
        log.info("유저 초기화에 걸린 시간: {}", System.currentTimeMillis() - before);
        before = System.currentTimeMillis();
        CreatedDataInfo createdStoreDataInfo = storeTestDataManageService.createTestStores(
                testDataCountRequirements.getStoreCount(), createdUserDataInfo);
        log.info("상점 초기화에 걸린 시간: {}", System.currentTimeMillis() - before);
        itemTestDataManageService.createTestItems(
                testDataCountRequirements.getItemCount(), createdUserDataInfo, createdStoreDataInfo, createdCategoryDataInfo);
    }

    @Transactional
    @Async
    public void clearAll() {
        itemTestDataManageService.clearAll();
        categoryManageService.clearAll();
        storeTestDataManageService.clearAll();
        userTestDataManageService.clearAll();
    }

    public CurrentTestDataCountsDto count() {
        return CurrentTestDataCountsDto.builder()
                .currentUsersCount(userTestDataManageService.count())
                .currentStoresCount(storeTestDataManageService.count())
                .currentCategoriesCount(categoryManageService.count())
                .currentItemsCount(itemTestDataManageService.countItems())
                .currentItemOptionsCount(itemTestDataManageService.countItemOptions())
                .build();
    }
}
