package com.example.flab.soft.shoppingmallfashion.item.domain;

import static com.example.flab.soft.shoppingmallfashion.util.NotNullValidator.*;

import com.example.flab.soft.shoppingmallfashion.category.Category;
import com.example.flab.soft.shoppingmallfashion.common.BaseEntity;
import com.example.flab.soft.shoppingmallfashion.store.repository.Store;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    private Integer price;
    private Integer discountAppliedPrice;
    private String description;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Sex sex;
    @Enumerated(value = EnumType.STRING)
    private SaleState saleState;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Column(nullable = false)
    private Long lastlyModifiedBy;

    @Builder
    public Item(String name, Integer price, Integer discountAppliedPrice, String description, Sex sex,
                SaleState saleState,
                Store store, Category category, Long lastlyModifiedBy) {
        this.name = requireNotNull(name);
        this.price = requireNotNull(price);
        this.discountAppliedPrice = discountAppliedPrice;
        this.description = description;
        this.sex = requireNotNull(sex);
        this.saleState = requireNotNull(saleState);
        this.store = requireNotNull(store);
        this.category = requireNotNull(category);
        this.lastlyModifiedBy = requireNotNull(lastlyModifiedBy);
    }
}
