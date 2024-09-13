package com.example.flab.soft.shoppingmallfashion.admin.service;

import com.example.flab.soft.shoppingmallfashion.admin.dto.CreatedDataInfo;
import com.example.flab.soft.shoppingmallfashion.admin.util.ConcurrentUtil;
import com.example.flab.soft.shoppingmallfashion.category.Category;
import com.example.flab.soft.shoppingmallfashion.category.CategoryRepository;
import com.example.flab.soft.shoppingmallfashion.category.LargeCategory;
import com.example.flab.soft.shoppingmallfashion.category.LargeCategoryRepository;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@RequiredArgsConstructor
public class CategoryManageService {
    private final CategoryRepository categoryRepository;
    private final LargeCategoryRepository largeCategoryRepository;
    private final ExecutorService executorService;
    private final TransactionTemplate txTemplate;

    @Transactional
    public CreatedDataInfo initCategory() {
        ConcurrentUtil.collect(Arrays.stream(TestLargeCategory.values())
                .map(largeCategory -> executorService.submit(() ->
                        txTemplate.executeWithoutResult(status ->
                                saveLargeCategoryAsGroup(largeCategory))))
                .toList());
        return CreatedDataInfo.builder()
                .createdCount(categoryRepository.count())
                .firstElementId(categoryRepository.findFirstBy().getId())
                .build();
    }

    private void saveLargeCategoryAsGroup(TestLargeCategory testLargeCategory) {
        LargeCategory largeCategory = largeCategoryRepository.save(
                LargeCategory.builder()
                        .name(testLargeCategory.getLargeCategory())
                        .build());
        testLargeCategory.getSubCategories().stream()
                .map(testCategory ->
                        Category.builder()
                                .largeCategory(largeCategory)
                                .name(testCategory.getCategoryName())
                                .build())
                .forEach(categoryRepository::save);
    }

    @Transactional
    public Long clearAll() {
        categoryRepository.deleteAll();
        largeCategoryRepository.deleteAll();
        return categoryRepository.count();
    }
}
