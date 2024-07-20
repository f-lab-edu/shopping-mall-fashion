package com.example.flab.soft.shoppingmallfashion.item.domain;

import static com.example.flab.soft.shoppingmallfashion.util.NotNullValidator.requireNotNull;

import com.example.flab.soft.shoppingmallfashion.common.BaseEntity;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.item.controller.ProductDto;
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
    @Column(name = "stocks_cnt")
    private Long stocksCount = 0L;

    @Builder
    public Product(String name, String size, String option, Item item, SaleState saleState, Long stocksCount) {
        this.name = requireNotNull(name);
        this.size = requireNotNull(size);
        this.option = option;
        this.item = requireNotNull(item);
        this.saleState = saleState;
        this.stocksCount = requireNotNull(stocksCount);
    }

    public void beSoldOut(Boolean isTemporarily) {
        if (isTemporarily) {
            saleState = SaleState.TEMPORARILY_SOLD_OUT;
        } else saleState = SaleState.SOLD_OUT;

        if (item.isAllProductsSoldOut()) {
            item.changeSaleState(SaleState.SOLD_OUT);
        }
    }

    public boolean isSoldOut() {
        return saleState == SaleState.SOLD_OUT || saleState == SaleState.TEMPORARILY_SOLD_OUT;
    }

    public boolean isTempSoldOut() {
        return saleState == SaleState.TEMPORARILY_SOLD_OUT;
    }

    public boolean isEndOfProduction() {
        return saleState == SaleState.END_OF_PRODUCTION;
    }

    public void endProduction() {
        saleState = SaleState.END_OF_PRODUCTION;
    }

    public boolean isOnSale() {
        return saleState == SaleState.ON_SALE;
    }

    public boolean startSale() {
        if (isOutOfStock()) {
            return false;
        }
        saleState = SaleState.ON_SALE;
        if (!item.isOnSale()) {
            item.changeSaleState(SaleState.ON_SALE);
        }
        return true;
    }

    public boolean isOutOfStock() {
        return stocksCount == 0;
    }

    public static Product of(Item item, ProductDto productDto) {
        return builder()
                .name(productDto.getName())
                .size(productDto.getSize())
                .option(productDto.getOption())
                .item(item)
                .saleState(productDto.getSaleState())
                .stocksCount(productDto.getStocksCount())
                .build();
    }
}
