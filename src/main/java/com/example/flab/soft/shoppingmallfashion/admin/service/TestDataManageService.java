package com.example.flab.soft.shoppingmallfashion.admin.service;

import com.example.flab.soft.shoppingmallfashion.admin.dto.TestDataCountRequirements;
import com.example.flab.soft.shoppingmallfashion.admin.dto.CreatedDataInfo;
import com.example.flab.soft.shoppingmallfashion.admin.dto.CurrentTestDataCountsDto;
import com.example.flab.soft.shoppingmallfashion.admin.dto.ItemCountsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TestDataManageService {
    private final UserTestDataManageService userTestDataManageService;
    private final StoreTestDataManageService storeTestDataManageService;
    private final CategoryManageService categoryManageService;
    private final ItemTestDataManageService itemTestDataManageService;

    public CurrentTestDataCountsDto init(TestDataCountRequirements testDataCountRequirements) {
        CreatedDataInfo createdCategoryDataInfo = categoryManageService.initCategory();
        CreatedDataInfo createdUserDataInfo = userTestDataManageService.createTestUsers(testDataCountRequirements.getUserCount());
        CreatedDataInfo createdStoreDataInfo = storeTestDataManageService.createTestStores(
                testDataCountRequirements.getStoreCount(), createdUserDataInfo);

        ItemCountsDto itemCountsDto = itemTestDataManageService.createTestItems(
                testDataCountRequirements.getItemCount(), createdUserDataInfo, createdStoreDataInfo, createdCategoryDataInfo);
        return CurrentTestDataCountsDto.builder()
                .currentUsersCount(createdUserDataInfo.getCreatedCount())
                .currentStoresCount(createdStoreDataInfo.getCreatedCount())
                .currentCategoriesCount(createdCategoryDataInfo.getCreatedCount())
                .currentItemsCount(itemCountsDto.getItemCount())
                .currentItemOptionsCount(itemCountsDto.getItemOptionCount())
                .build();
    }

    @Transactional
    public CurrentTestDataCountsDto clearAll() {
        ItemCountsDto itemCountsDto = itemTestDataManageService.clearAll();
        Long categoryCount = categoryManageService.clearAll();
        Long storeCount = storeTestDataManageService.clearAll();
        Long userCount = userTestDataManageService.clearAll();

        return CurrentTestDataCountsDto.builder()
                .currentUsersCount(userCount)
                .currentStoresCount(storeCount)
                .currentCategoriesCount(categoryCount)
                .currentItemsCount(itemCountsDto.getItemCount())
                .currentItemOptionsCount(itemCountsDto.getItemOptionCount())
                .build();
    }
}
