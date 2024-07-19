package com.example.flab.soft.shoppingmallfashion.item.service;

import static org.assertj.core.api.Assertions.*;

import com.example.flab.soft.shoppingmallfashion.category.Category;
import com.example.flab.soft.shoppingmallfashion.category.CategoryRepository;
import com.example.flab.soft.shoppingmallfashion.item.controller.ItemCreateRequest;
import com.example.flab.soft.shoppingmallfashion.item.controller.ProductDto;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemRepository;
import com.example.flab.soft.shoppingmallfashion.item.repository.ProductRepository;
import java.util.List;
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
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ProductRepository productRepository;
    private static final long USER_ID = 1L;


    @Test
    @DisplayName("상품 등록")
    void addNewItem() {
        Category category = categoryRepository.findById(1L).get();
        Long itemCountBefore = category.getItemCount();
        Long itemId = itemCommandService.addItem(ItemCreateRequest.builder()
                .name("new item")
                .originalPrice(1000)
                .salePrice(1000)
                .sex("UNISEX")
                .saleState("PREPARING")
                .products(List.of(ProductDto.builder()
                        .name("new item red")
                        .size("L")
                        .option("red")
                        .saleState("ON_SALE")
                        .build()))
                .storeId(1L)
                .categoryId(1L)
                .build(), USER_ID);

        assertThat(itemRepository.existsById(itemId)).isTrue();
        assertThat(itemRepository.findById(itemId).get().getProducts().get(0))
                .hasFieldOrPropertyWithValue("name", "new item red");
        assertThat(category.getItemCount()).isEqualTo(itemCountBefore + 1);
    }
}