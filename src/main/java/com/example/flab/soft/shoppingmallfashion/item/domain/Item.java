package com.example.flab.soft.shoppingmallfashion.item.domain;

import static com.example.flab.soft.shoppingmallfashion.util.NotNullValidator.requireNotNull;

import com.example.flab.soft.shoppingmallfashion.category.Category;
import com.example.flab.soft.shoppingmallfashion.common.BaseEntity;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.item.controller.ItemCreateRequest;
import com.example.flab.soft.shoppingmallfashion.store.repository.Store;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Item extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer originalPrice;
    private Integer salePrice;
    private String description;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Sex sex;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private SaleState saleState;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST)
    private List<ItemOption> itemOptions = new ArrayList<>();
    @Embedded
    private ItemStats itemStats;
    @Column(nullable = false)
    private Long lastlyModifiedBy;
    @OneToMany(mappedBy = "itemId", fetch = FetchType.LAZY)
    private List<ItemSearchKeyword> itemSearchKeywords = new ArrayList<>();

    @Builder
    public Item(String name, Integer originalPrice, Integer salePrice, String description, Sex sex,
                SaleState saleState,
                Store store, Category category, Long lastlyModifiedBy) {
        this.name = requireNotNull(name);
        this.originalPrice = requireNotNull(originalPrice);
        this.salePrice = requireNotNull(salePrice);
        this.description = description;
        this.sex = requireNotNull(sex);
        this.saleState = requireNotNull(saleState);
        this.store = requireNotNull(store);
        this.category = requireNotNull(category);
        this.itemStats = new ItemStats();
        this.lastlyModifiedBy = requireNotNull(lastlyModifiedBy);
    }

    public void addItemOption(ItemOption itemOption) {
        if (!itemOptions.contains(itemOption)) {
            itemOptions.add(itemOption);
        }
    }

    public boolean isAllOptionsSoldOut() {
        return itemOptions.stream().allMatch((ItemOption::isSoldOut));
    }

    public void renewSaleState() {
        if (isAllOptionsSoldOut()) {
            saleState = SaleState.SOLD_OUT;
        }
    }

    public boolean hasProductTempSoldOut() {
        return itemOptions.stream().anyMatch((ItemOption::isTempSoldOut));
    }

    public void endProduction() {
        if (saleState == SaleState.END_OF_PRODUCTION) {
            throw new ApiException(ErrorEnum.ALREADY_END_OF_PRODUCTION);
        }
        saleState = SaleState.END_OF_PRODUCTION;
        itemOptions.forEach(ItemOption::endProduction);
    }

    public boolean isEndOfProduction() {
        return saleState.equals(SaleState.END_OF_PRODUCTION);
    }

    public List<ItemOption> startAllSale() {
        boolean isAllOptionsOOS = itemOptions.stream().allMatch(ItemOption::isOutOfStock);
        if (isAllOptionsOOS) {
            throw new ApiException(ErrorEnum.OUT_OF_STOCK);
        }
        itemOptions.forEach(ItemOption::startSale);
        saleState = SaleState.ON_SALE;

        return itemOptions;
    }

    public void changeSaleState(SaleState saleState) {
        this.saleState = saleState;
    }

    public boolean isOnSale() {
        return saleState == SaleState.ON_SALE;
    }

    public Integer addItemSearchKeyword(ItemSearchKeyword itemSearchKeyword) {
        if (itemSearchKeywords.contains(itemSearchKeyword)) {
            throw new ApiException(ErrorEnum.DUPLICATED_ITEM_SEARCH_TAG);
        }
        itemSearchKeywords.add(itemSearchKeyword);
        return itemSearchKeywords.size();
    }

    public List<String> selectDefaultKeywords() {
        String[] keywordsInName = name.split(" ");
        String storeName = store.getName();
        String categoryName = category.getName();
        String largeCategoryName = category.getLargeCategory().getName();

        List<String> defaultKeywords = new ArrayList<>(keywordsInName.length + 3);

        defaultKeywords.addAll(Arrays.stream(keywordsInName).toList());
        defaultKeywords.add(storeName);
        defaultKeywords.add(categoryName);
        defaultKeywords.add(largeCategoryName);

        return defaultKeywords.stream().distinct().toList();
    }

    public void modifyOrderCount(Long orderCount) {
        itemStats.modifyOrderCount(orderCount);
    }

    public static Item of(Category category, Store store, ItemCreateRequest itemCreateRequest, Long userId) {
        return builder()
                .name(itemCreateRequest.getName())
                .originalPrice(itemCreateRequest.getOriginalPrice())
                .salePrice(itemCreateRequest.getSalePrice())
                .description(itemCreateRequest.getDescription())
                .sex(itemCreateRequest.getSex())
                .saleState(itemCreateRequest.getSaleState())
                .store(store)
                .category(category)
                .lastlyModifiedBy(userId)
                .build();
    }
}
