package com.example.flab.soft.shoppingmallfashion.item.service;

import static org.assertj.core.api.Assertions.*;

import com.example.flab.soft.shoppingmallfashion.category.Category;
import com.example.flab.soft.shoppingmallfashion.category.CategoryRepository;
import com.example.flab.soft.shoppingmallfashion.item.controller.ItemCreateRequest;
import com.example.flab.soft.shoppingmallfashion.item.controller.ProductDto;
import com.example.flab.soft.shoppingmallfashion.item.domain.Item;
import com.example.flab.soft.shoppingmallfashion.item.domain.Product;
import com.example.flab.soft.shoppingmallfashion.item.domain.SaleState;
import com.example.flab.soft.shoppingmallfashion.item.domain.Sex;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemRepository;
import com.example.flab.soft.shoppingmallfashion.item.repository.ProductRepository;
import com.example.flab.soft.shoppingmallfashion.store.repository.Store;
import com.example.flab.soft.shoppingmallfashion.store.repository.StoreRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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
    StoreRepository storeRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ProductRepository productRepository;
    private static final long USER_ID = 1L;
    private static final ItemCreateRequest ITEM_CREATE_REQUEST = ItemCreateRequest.builder()
            .name("new item")
            .originalPrice(1000)
            .salePrice(1000)
            .sex(Sex.MEN)
            .saleState(SaleState.ON_SALE)
            .products(List.of(ProductDto.builder()
                    .name("new item red")
                    .size("L")
                    .option("red")
                    .saleState(SaleState.ON_SALE)
                    .build()))
            .storeId(1L)
            .categoryId(1L)
            .build();

    Category category;
    Store store;
    Item item;
    Product product;
    @BeforeEach
    void setUp() {
        store = storeRepository.findById(1L).get();
        category = categoryRepository.findById(1L).get();
        item = itemRepository.save(Item.builder()
                .name("test item")
                .originalPrice(1000)
                .salePrice(900)
                .sex(Sex.MEN)
                .saleState(SaleState.ON_SALE)
                .store(store)
                .category(category)
                .lastlyModifiedBy(1L)
                .build());

        product = Product.builder()
                .name("test product")
                .size("L")
                .option("red")
                .item(item)
                .saleState(SaleState.ON_SALE)
                .build();
        productRepository.save(product);
        item.addProduct(product);
    }

    @Test
    @DisplayName("상품 등록")
    void addNewItem() {
        Long itemCountBefore = category.getItemCount();
        Long itemId = itemCommandService.addItem(ITEM_CREATE_REQUEST, USER_ID);

        assertThat(itemRepository.existsById(itemId)).isTrue();
        assertThat(itemRepository.findById(itemId).get().getProducts().get(0))
                .hasFieldOrPropertyWithValue("name", "new item red");
        assertThat(category.getItemCount()).isEqualTo(itemCountBefore + 1);
    }

    @Test
    @DisplayName("상품 품절 처리")
    void soldOut() {
        Boolean isTemporarilySoldOut = false;

        itemCommandService.updateToSoldOut(product.getId(), isTemporarilySoldOut);

        assertThat(product.isSoldOut()).isTrue();
        assertThat(item.isAllProductsSoldOut()).isTrue();
    }

    @Test
    @DisplayName("상품 일시 품절 처리")
    void soldOutTemporarily() {
        Boolean isTemporarilySoldOut = true;

        itemCommandService.updateToSoldOut(product.getId(), isTemporarilySoldOut);

        assertThat(product.isSoldOut()).isTrue();
        assertThat(item.isAllProductsSoldOut()).isTrue();
        assertThat(item.hasProductTempSoldOut()).isTrue();
    }

    @Test
    @DisplayName("상품 단종 처리")
    void endOfProduction() {
        itemCommandService.endProduction(item.getId());

        assertThat(product.isEndOfProduction()).isTrue();
        assertThat(item.isEndOfProduction()).isTrue();
    }

    @Test
    @DisplayName("아이템 판매 시작")
    void startSale() {
        itemCommandService.startSale(item.getId());

        assertThat(item.isOnSale()).isTrue();
        assertThat(product.isOnSale()).isTrue();
    }

    @Test
    @DisplayName("일부 상품 판매 재개")
    void restartSale() {
        itemCommandService.restartSale(product.getId());

        assertThat(item.isOnSale()).isTrue();
        assertThat(product.isOnSale()).isTrue();
    }
}