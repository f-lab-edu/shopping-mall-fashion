package com.example.flab.soft.shoppingmallfashion.item;

import static org.assertj.core.api.Assertions.*;

import com.example.flab.soft.shoppingmallfashion.category.Category;
import com.example.flab.soft.shoppingmallfashion.category.LargeCategory;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.item.domain.Item;
import com.example.flab.soft.shoppingmallfashion.item.domain.ItemOption;
import com.example.flab.soft.shoppingmallfashion.item.domain.SaleState;
import com.example.flab.soft.shoppingmallfashion.item.domain.Sex;
import com.example.flab.soft.shoppingmallfashion.store.repository.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ItemDomainTest {
    private Store store;
    private Category category;
    private LargeCategory largeCategory;
    @BeforeEach
    void setUp() {
        store = Store.builder()
                .name("name")
                .logo("logo")
                .description("description")
                .businessRegistrationNumber("0123456789")
                .managerId(1L)
                .build();

        largeCategory = LargeCategory.builder()
                .name("large category")
                .build();

        category = Category.builder()
                .name("category")
                .largeCategory(largeCategory)
                .build();
    }

    @Test
    @DisplayName("준비중에서 판매 시작으로 변경시 아이템 옵션들의 판매 상태도 모두 판매 시작으로 변경")
    void whenItemStartAllSale_thenItemOptionsAlsoStartSale() {
        //given
        Item item = Item.builder()
                .name("test item")
                .originalPrice(1000)
                .salePrice(900)
                .sex(Sex.MEN)
                .saleState(SaleState.PREPARING)
                .store(store)
                .category(category)
                .lastlyModifiedBy(1L)
                .build();

        ItemOption itemOption = ItemOption.builder()
                .name("test product")
                .size("L")
                .optionValue("red")
                .item(item)
                .saleState(SaleState.PREPARING)
                .stocksCount(10L)
                .build();

        item.addItemOption(itemOption);

        //when
        item.startAllSale();

        //then
        assertThat(item.getSaleState()).isEqualTo(SaleState.ON_SALE);
        assertThat(item.getItemOptions().get(0).getSaleState()).isEqualTo(SaleState.ON_SALE);
    }

    @Test
    @DisplayName("판매 시작시 모든 옵션이 품절 상태이면 OUT_OF_STOCK 예외")
    void whenAllOptionsSoldOutInStartSale_thenThrowException() {
        //given
        Item item = Item.builder()
                .name("test item")
                .originalPrice(1000)
                .salePrice(900)
                .sex(Sex.MEN)
                .saleState(SaleState.PREPARING)
                .store(store)
                .category(category)
                .lastlyModifiedBy(1L)
                .build();

        ItemOption itemOption = ItemOption.builder()
                .name("test product")
                .size("L")
                .optionValue("red")
                .item(item)
                .saleState(SaleState.PREPARING)
                .stocksCount(0L)
                .build();

        item.addItemOption(itemOption);

        //then
        assertThatThrownBy(item::startAllSale).hasMessage(ErrorEnum.OUT_OF_STOCK.getMessage());
        assertThat(item.getSaleState()).isNotEqualTo(SaleState.ON_SALE);
    }

    @Test
    @DisplayName("상품 단종시 모든 옵션 판매 상태 단종으로 변경")
    void whenItemProductionEnds_thenAllOptionsEndProduction() {
        //given
        Item item = Item.builder()
                .name("test item")
                .originalPrice(1000)
                .salePrice(900)
                .sex(Sex.MEN)
                .saleState(SaleState.PREPARING)
                .store(store)
                .category(category)
                .lastlyModifiedBy(1L)
                .build();

        ItemOption itemOption = ItemOption.builder()
                .name("test product")
                .size("L")
                .optionValue("red")
                .item(item)
                .saleState(SaleState.PREPARING)
                .stocksCount(10L)
                .build();

        item.addItemOption(itemOption);

        //when
        item.endProduction();

        //then
        assertThat(item.getSaleState()).isEqualTo(SaleState.END_OF_PRODUCTION);
        assertThat(item.getItemOptions().get(0).getSaleState()).isEqualTo(SaleState.END_OF_PRODUCTION);
    }

    @Test
    @DisplayName("판매 단종 상태인 상품을 판매 단종시 예외")
    void whenAlreadyEndProduction_thenThrowException() {
        //given
        Item item = Item.builder()
                .name("test item")
                .originalPrice(1000)
                .salePrice(900)
                .sex(Sex.MEN)
                .saleState(SaleState.END_OF_PRODUCTION)
                .store(store)
                .category(category)
                .lastlyModifiedBy(1L)
                .build();

        //then
        assertThatThrownBy(item::endProduction).hasMessage(ErrorEnum.ALREADY_END_OF_PRODUCTION.getMessage());
    }
}
