package com.example.flab.soft.shoppingmallfashion.item.domain;

import static com.example.flab.soft.shoppingmallfashion.util.NotNullValidator.requireNotNull;

import com.example.flab.soft.shoppingmallfashion.common.BaseEntity;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.item.controller.ItemOptionDto;
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

@Entity(name = "item_options")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = {"name", "size", "optionValue"}, callSuper = false)
public class ItemOption extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String size;
    private String optionValue;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private SaleState saleState;
    @Column(name = "stocks_count")
    private Long stocksCount = 0L;

    @Builder
    public ItemOption(String name, String size, String optionValue, Item item, SaleState saleState, Long stocksCount) {
        this.name = requireNotNull(name);
        this.size = requireNotNull(size);
        this.optionValue = optionValue;
        this.item = requireNotNull(item);
        this.saleState = saleState;
        this.stocksCount = requireNotNull(stocksCount);
    }

    public Long reduceStocksCount(Integer amount) {
        return stocksCount -= amount;
    }

    public void beSoldOut(Boolean isTemporarily) {
        if (isSoldOut()) {
            throw new ApiException(ErrorEnum.ALREADY_SOLD_OUT);
        }
        if (isTemporarily) {
            saleState = SaleState.TEMPORARILY_SOLD_OUT;
        } else {
            saleState = SaleState.SOLD_OUT;
        }

        if (item.isAllOptionsSoldOut()) {
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
        if (isOnSale()) {
            throw new ApiException(ErrorEnum.ALREADY_ON_SALE);
        }
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

    public static ItemOption of(Item item, ItemOptionDto itemOptionDto) {
        return builder()
                .name(itemOptionDto.getName())
                .size(itemOptionDto.getSize())
                .optionValue(itemOptionDto.getOption())
                .item(item)
                .saleState(itemOptionDto.getSaleState())
                .stocksCount(itemOptionDto.getStocksCount())
                .build();
    }
}
