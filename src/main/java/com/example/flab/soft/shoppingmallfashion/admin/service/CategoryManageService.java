package com.example.flab.soft.shoppingmallfashion.admin.service;

import com.example.flab.soft.shoppingmallfashion.admin.dto.CreatedDataInfo;
import com.example.flab.soft.shoppingmallfashion.category.CategoryRepository;
import com.example.flab.soft.shoppingmallfashion.category.LargeCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryManageService {
    private final CategoryRepository categoryRepository;
    private final LargeCategoryRepository largeCategoryRepository;
    private final AdminBatchService adminBatchService;

    @Transactional
    public CreatedDataInfo initCategory() {
        adminBatchService.bulkInsertCategories();
        return CreatedDataInfo.builder()
                .createdCount((long) TestLargeCategory.getCategories().size())
                .firstElementId(categoryRepository.findFirstBy().getId())
                .build();
    }

    @Transactional
    public void clearAll() {
        categoryRepository.deleteAll();
        largeCategoryRepository.deleteAll();
    }

    public Long count() {
        return categoryRepository.count();
    }
}
