package com.example.flab.soft.shoppingmallfashion.item.service;

import static org.assertj.core.api.Assertions.*;

import com.example.flab.soft.shoppingmallfashion.category.Category;
import com.example.flab.soft.shoppingmallfashion.category.CategoryRepository;
import com.example.flab.soft.shoppingmallfashion.item.controller.ItemCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ItemCommandServiceTest {
    @Autowired
    ItemCommandService itemCommandService;
    @Autowired
    CategoryRepository categoryRepository;
    private static final long USER_ID = 1L;


    @Test
    @DisplayName("상품 등록시 카테고리의 상품수 증가")
    void whenAddNewItem_thenIncreaseCategoryItemCount() {
        Category category = categoryRepository.findById(1L).get();
        Long itemCountBefore = category.getItemCount();
        itemCommandService.addItem(ItemCreateRequest.builder()
                .name("name")
                .price(1000)
                .sex("UNISEX")
                .saleState("PREPARING")
                .storeId(1L)
                .categoryId(1L)
                .build(), USER_ID);
        Long itemCountAfter = category.getItemCount();

        assertThat(itemCountAfter).isEqualTo(itemCountBefore + 1);
    }
}