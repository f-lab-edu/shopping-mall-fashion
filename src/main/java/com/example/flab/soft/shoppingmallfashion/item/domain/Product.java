package com.example.flab.soft.shoppingmallfashion.item.domain;

import static com.example.flab.soft.shoppingmallfashion.util.NotNullValidator.requireNotNull;

import com.example.flab.soft.shoppingmallfashion.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = {"name", "size", "option"}, callSuper = false)
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String size;
    private String option;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private SaleState saleState;

    @Builder
    public Product(String name, String size, String option, Item item, SaleState saleState) {
        this.name = requireNotNull(name);
        this.size = requireNotNull(size);
        this.option = option;
        this.item = requireNotNull(item);
        this.saleState = saleState;
    }

    public void beSoldOut(Boolean isTemporarily) {
        if (isTemporarily) {
            saleState = SaleState.TEMPORARILY_SOLD_OUT;
        } else saleState = SaleState.SOLD_OUT;
    }

    public boolean isSoldOut() {
        return saleState.equals(SaleState.SOLD_OUT) || saleState.equals(SaleState.TEMPORARILY_SOLD_OUT);
    }

    public boolean isTempSoldOut() {
        return saleState.equals(SaleState.TEMPORARILY_SOLD_OUT);
    }
}
