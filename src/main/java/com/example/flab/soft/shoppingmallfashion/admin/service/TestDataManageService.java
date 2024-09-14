package com.example.flab.soft.shoppingmallfashion.admin.service;

import com.example.flab.soft.shoppingmallfashion.admin.dto.TestDataCountRequirements;
import com.example.flab.soft.shoppingmallfashion.admin.dto.CreatedDataInfo;
import com.example.flab.soft.shoppingmallfashion.admin.dto.CurrentTestDataCountsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TestDataManageService {
    private final UserTestDataManageService userTestDataManageService;
    private final StoreTestDataManageService storeTestDataManageService;
    private final CategoryManageService categoryManageService;
    private final ItemTestDataManageService itemTestDataManageService;

    public void init(TestDataCountRequirements testDataCountRequirements) {
        CreatedDataInfo createdCategoryDataInfo = categoryManageService.initCategory();
        CreatedDataInfo createdUserDataInfo = userTestDataManageService.createTestUsers(testDataCountRequirements.getUserCount());
        CreatedDataInfo createdStoreDataInfo = storeTestDataManageService.createTestStores(
                testDataCountRequirements.getStoreCount(), createdUserDataInfo);

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
