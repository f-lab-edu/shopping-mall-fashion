package com.example.flab.soft.shoppingmallfashion.item;

import static org.assertj.core.api.Assertions.*;

import com.example.flab.soft.shoppingmallfashion.category.Category;
import com.example.flab.soft.shoppingmallfashion.category.CategoryRepository;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.item.controller.ItemCreateRequest;
import com.example.flab.soft.shoppingmallfashion.item.controller.ItemOptionDto;
import com.example.flab.soft.shoppingmallfashion.item.domain.Item;
import com.example.flab.soft.shoppingmallfashion.item.domain.ItemOption;
import com.example.flab.soft.shoppingmallfashion.item.domain.ItemSearchKeyword;
import com.example.flab.soft.shoppingmallfashion.item.domain.SaleState;
import com.example.flab.soft.shoppingmallfashion.item.domain.SearchKeyword;
import com.example.flab.soft.shoppingmallfashion.item.domain.Sex;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemRepository;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemOptionRepository;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemSearchKeywordRepository;
import com.example.flab.soft.shoppingmallfashion.item.repository.SearchKeywordRepository;
import com.example.flab.soft.shoppingmallfashion.item.service.ItemCommandService;
import com.example.flab.soft.shoppingmallfashion.item.service.ItemSearchKeywordService;
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
class ItemServiceTest {
    @Autowired
    ItemCommandService itemCommandService;
    @Autowired
    ItemSearchKeywordService itemSearchKeywordService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemOptionRepository itemOptionRepository;
    @Autowired
    ItemSearchKeywordRepository itemSearchKeywordRepository;
    @Autowired
    SearchKeywordRepository searchKeywordRepository;
    private static final long USER_ID = 1L;
    private static final ItemCreateRequest ITEM_CREATE_REQUEST = ItemCreateRequest.builder()
            .name("nike wind jacket")
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
                .optionValue("red")
                .item(item)
                .saleState(SaleState.PREPARING)
                .stocksCount(10L)
                .build();

        OOSItemOption = ItemOption.builder()
                .name("test product2")
                .size("L")
                .optionValue("blue")
                .item(item)
                .saleState(SaleState.SOLD_OUT)
                .stocksCount(0L)
                .build();
        itemOptionRepository.save(itemOption);
        itemOptionRepository.save(OOSItemOption);
        item.addItemOption(itemOption);
        item.addItemOption(OOSItemOption);
    }

    @Test
    @DisplayName("상품 등록")
    void addNewItem() {
        Long itemCountBefore = category.getItemCount();
        Long itemId = itemCommandService.addItem(ITEM_CREATE_REQUEST, USER_ID).getItemId();

        assertThat(itemRepository.existsById(itemId)).isTrue();
        assertThat(itemRepository.findById(itemId).get().getItemOptions().get(0))
                .hasFieldOrPropertyWithValue("name", "new item red");
        assertThat(category.getItemCount()).isEqualTo(itemCountBefore + 1);
    }

    @Test
    @DisplayName("상품 등록시 상품 이름의 각 어절, 스토어명과 대분류, 분류명이 모두 기본 검색 키워드로 등록된다.")
    void addDefaultKeywords_whenAddItem() {
        Long itemId = itemCommandService.addItem(ITEM_CREATE_REQUEST, USER_ID).getItemId();

        Item newItem = itemRepository.findById(itemId).get();
        List<String> defaultKeywords = newItem.getItemSearchKeywords().stream()
                .map(ItemSearchKeyword::getKeyword)
                .toList();
        assertThat(defaultKeywords).contains(newItem.getCategory().getName());
        assertThat(defaultKeywords).contains(newItem.getCategory().getLargeCategory().getName());
        assertThat(defaultKeywords).contains(newItem.getStore().getName());
        assertThat(defaultKeywords).contains(newItem.getName().split(" "));
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
        assertThatThrownBy(() -> itemCommandService.restartSale(OOSItemOption.getId()))
                .isInstanceOf(ApiException.class);
    }

    @Test
    @DisplayName("재고 추가")
    void change_stocks_count() {
        itemCommandService.addStocks(OOSItemOption.getId(), 10);

        assertThat(itemOptionRepository.findById(OOSItemOption.getId()).get().getStocksCount()).isEqualTo(10);
    }

    @Test
    @DisplayName("상품 검색 키워드 추가")
    void change_item_search_keywords() {
        itemSearchKeywordService.updateItemSearchKeyword(item.getId(), List.of("pants", "jackets"));

        SearchKeyword keyword1 = searchKeywordRepository.findByName("pants").get();
        SearchKeyword keyword2  = searchKeywordRepository.findByName("jackets").get();
        assertThat(keyword1).isNotNull();
        assertThat(keyword2).isNotNull();
        assertThat(item.getItemSearchKeywords()).contains(
                itemSearchKeywordRepository.findByItemIdAndSearchKeyword(
                        item.getId(), keyword1).get());
        assertThat(item.getItemSearchKeywords()).contains(
                itemSearchKeywordRepository.findByItemIdAndSearchKeyword(
                        item.getId(), keyword2).get());
    }

    @Test
    @DisplayName("상품 검색 키워드 변경시 기본 키워드는 변경되지 않는다")
    void change_item_search_tags() {
        //given
        SearchKeyword defaultSearchKeyword = SearchKeyword.builder()
                .name("wind jacket")
                .build();

        SearchKeyword oldSearchKeyword = SearchKeyword.builder()
                .name("nike")
                .build();

        searchKeywordRepository.save(defaultSearchKeyword);
        searchKeywordRepository.save(oldSearchKeyword);

        ItemSearchKeyword defaultKeyword = ItemSearchKeyword.builder()
                .itemId(item.getId())
                .searchKeyword(defaultSearchKeyword)
                .isDefault(true)
                .build();

        ItemSearchKeyword nonDefaultKeyword = ItemSearchKeyword.builder()
                .itemId(item.getId())
                .searchKeyword(oldSearchKeyword)
                .isDefault(true)
                .build();

        itemSearchKeywordRepository.save(defaultKeyword);
        itemSearchKeywordRepository.save(nonDefaultKeyword);

        String newSearchKeyword = "addidas";

        //when
        itemSearchKeywordService.updateItemSearchKeyword(item.getId(),
                List.of(defaultSearchKeyword.getName(), newSearchKeyword));

        //then
        List<String> itemKeywords = item.getItemSearchKeywords().stream()
                .map(ItemSearchKeyword::getKeyword)
                .toList();

        assertThat(itemKeywords).contains(defaultKeyword.getKeyword());
        assertThat(itemKeywords).contains(newSearchKeyword);
        assertThat(itemKeywords).doesNotContain(oldSearchKeyword.getName());
    }
}