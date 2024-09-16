package com.example.flab.soft.shoppingmallfashion.admin.util;

import com.example.flab.soft.shoppingmallfashion.admin.dto.CreatedDataInfo;
import com.example.flab.soft.shoppingmallfashion.admin.dto.TestItemDto;
import com.example.flab.soft.shoppingmallfashion.item.controller.ItemOptionDto;
import com.example.flab.soft.shoppingmallfashion.item.domain.SaleState;
import com.example.flab.soft.shoppingmallfashion.item.domain.Sex;
import java.util.*;
import java.util.stream.Collectors;
import java.util.UUID;

public class ItemDtoGenerator {
    private static final Random random = new Random();

    public static TestItemDto generateItemTestDtos(
            CreatedDataInfo userCreatedDataInfo,
            CreatedDataInfo storeCreatedDataInfo,
            CreatedDataInfo categoryCreatedDataInfo) {
        long userId = random.nextLong(userCreatedDataInfo.getCreatedCount()) + userCreatedDataInfo.getFirstElementId();
        long storeId = random.nextLong(storeCreatedDataInfo.getCreatedCount()) + storeCreatedDataInfo.getFirstElementId();

        long categoryId = random.nextLong(categoryCreatedDataInfo.getCreatedCount()) + categoryCreatedDataInfo.getFirstElementId();

        String name = "store" + storeId + " " + "category" + categoryId + " " + UUID.randomUUID();

        int originalPrice = random.nextInt(300000 - 5000 + 1) + 5000;
        int salePrice = originalPrice - 1000;

        String description = "description";

        Sex sex = Sex.values()[random.nextInt(Sex.values().length)];

        SaleState saleState = SaleState.ON_SALE;

        List<ItemOptionDto> itemOptions = generateItemOptions(name);

        int orderCount = random.nextInt(100000);

        return TestItemDto.builder()
                .name(name)
                .originalPrice(originalPrice)
                .salePrice(salePrice)
                .description(description)
                .sex(sex)
                .saleState(saleState)
                .storeId(storeId)
                .itemOptions(itemOptions)
                .categoryId(categoryId)
                .isModifiedBy(userId)
                .orderCount(orderCount)
                .build();
    }

    private static List<ItemOptionDto> generateItemOptions(String baseName) {
        String[] sizes = {"XS", "S", "M", "L", "XL"};
        return Arrays.stream(sizes)
                .map(size -> ItemOptionDto.builder()
                        .name(baseName + " " + size)
                        .size(size)
                        .option(null)
                        .saleState(SaleState.ON_SALE)
                        .stocksCount(1000L)
                        .build())
                .collect(Collectors.toList());
    }
}

