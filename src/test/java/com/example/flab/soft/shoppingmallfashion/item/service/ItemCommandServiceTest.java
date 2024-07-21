package com.example.flab.soft.shoppingmallfashion.item.service;

import static org.assertj.core.api.Assertions.*;

import com.example.flab.soft.shoppingmallfashion.category.Category;
import com.example.flab.soft.shoppingmallfashion.category.CategoryRepository;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.item.controller.ItemCreateRequest;
import com.example.flab.soft.shoppingmallfashion.item.controller.ItemOptionDto;
import com.example.flab.soft.shoppingmallfashion.item.domain.Item;
import com.example.flab.soft.shoppingmallfashion.item.domain.ItemOption;
import com.example.flab.soft.shoppingmallfashion.item.domain.SaleState;
import com.example.flab.soft.shoppingmallfashion.item.domain.Sex;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemRepository;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemOptionRepository;
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
    ItemOptionRepository itemOptionRepository;
    private static final long USER_ID = 1L;
    private static final ItemCreateRequest ITEM_CREATE_REQUEST = ItemCreateRequest.builder()
            .name("new item")
            .originalPrice(1000)
            .salePrice(1000)
            .sex(Sex.MEN)
            .saleState(SaleState.ON_SALE)
            .itemOptions(List.of(ItemOptionDto.builder()
                    .name("new item red")
                    .size("L")
                    .option("red")
                    .saleState(SaleState.ON_SALE)
                    .stocksCount(10L)
                    .build()))
            .storeId(1L)
            .categoryId(1L)
            .build();

    Category category;
    Store store;
    Item item;
    ItemOption itemOption;
    ItemOption OOSItemOption;
    @BeforeEach
    void setUp() {
        store = storeRepository.findById(1L).get();
        category = categoryRepository.findById(1L).get();
        item = itemRepository.save(Item.builder()
                .name("test item")
                .originalPrice(1000)
                .salePrice(900)
                .sex(Sex.MEN)
                .saleState(SaleState.PREPARING)
                .store(store)
                .category(category)
                .lastlyModifiedBy(1L)
                .build());

        itemOption = ItemOption.builder()
                .name("test product")
                .size("L")
                .option("red")
                .item(item)
                .saleState(SaleState.PREPARING)
                .stocksCount(10L)
                .build();

        OOSItemOption = ItemOption.builder()
                .name("test product2")
                .size("L")
                .option("blue")
                .item(item)
                .saleState(SaleState.SOLD_OUT)
                .stocksCount(0L)
                .build();
        itemOptionRepository.save(itemOption);
        itemOptionRepository.save(OOSItemOption);
        item.addProduct(itemOption);
        item.addProduct(OOSItemOption);
    }

    @Test
    @DisplayName("상품 등록")
    void addNewItem() {
        Long itemCountBefore = category.getItemCount();
        Long itemId = itemCommandService.addItem(ITEM_CREATE_REQUEST, USER_ID);

        assertThat(itemRepository.existsById(itemId)).isTrue();
        assertThat(itemRepository.findById(itemId).get().getItemOptions().get(0))
                .hasFieldOrPropertyWithValue("name", "new item red");
        assertThat(category.getItemCount()).isEqualTo(itemCountBefore + 1);
    }

    @Test
    @DisplayName("상품 품절 처리")
    void soldOut() {
        Boolean isTemporarilySoldOut = false;

        itemCommandService.updateToSoldOut(itemOption.getId(), isTemporarilySoldOut);

        assertThat(itemOption.isSoldOut()).isTrue();
        assertThat(item.isAllOptionsSoldOut()).isTrue();
    }

    @Test
    @DisplayName("상품 일시 품절 처리")
    void soldOutTemporarily() {
        Boolean isTemporarilySoldOut = true;

        itemCommandService.updateToSoldOut(itemOption.getId(), isTemporarilySoldOut);

        assertThat(itemOption.isSoldOut()).isTrue();
        assertThat(item.isAllOptionsSoldOut()).isTrue();
        assertThat(item.hasProductTempSoldOut()).isTrue();
    }

    @Test
    @DisplayName("상품 단종 처리")
    void endOfProduction() {
        itemCommandService.endProduction(item.getId());

        assertThat(itemOption.isEndOfProduction()).isTrue();
        assertThat(item.isEndOfProduction()).isTrue();
    }

    @Test
    @DisplayName("아이템 판매 시작")
    void startSale() {
        itemCommandService.startSale(item.getId());

        assertThat(item.isOnSale()).isTrue();
        assertThat(itemOption.isOnSale()).isTrue();
    }

    @Test
    @DisplayName("일부 상품 판매 재개")
    void restartSale() {
        itemCommandService.restartSale(itemOption.getId());

        assertThat(item.isOnSale()).isTrue();
        assertThat(itemOption.isOnSale()).isTrue();
    }

    @Test
    @DisplayName("판매 시작시 재고가 존재해야 한다")
    void whenStartSaleOOSProduct_throwException() {
        ItemOption oosItemOption = OOSItemOption;
        assertThatThrownBy(() -> itemCommandService.restartSale(oosItemOption.getId()))
                .isInstanceOf(ApiException.class);
    }
}