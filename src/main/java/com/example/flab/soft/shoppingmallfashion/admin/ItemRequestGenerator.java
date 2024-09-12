package com.example.flab.soft.shoppingmallfashion.admin;

import com.example.flab.soft.shoppingmallfashion.item.controller.ItemCreateRequest;
import com.example.flab.soft.shoppingmallfashion.item.controller.ItemOptionDto;
import com.example.flab.soft.shoppingmallfashion.item.domain.SaleState;
import com.example.flab.soft.shoppingmallfashion.item.domain.Sex;
import java.util.*;
import java.util.stream.Collectors;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ItemRequestGenerator {
    private static final Random random = new Random();

    public static ItemCreateRequest generateItemCreateRequest(CreatedDataInfo storeCreatedDataInfo, CreatedDataInfo categoryCreatedDataInfo) {
        // Generate storeId randomly within the range of storeCount
        long storeId = random.nextLong(storeCreatedDataInfo.getCreatedCount()) + storeCreatedDataInfo.getFirstElementId();

        // Generate categoryId randomly within the range of TestCategory values length
        long categoryId = random.nextLong(categoryCreatedDataInfo.getCreatedCount()) + categoryCreatedDataInfo.getFirstElementId();

        // Create random name using storeId, categoryId, and random UUID
        String name = "store" + storeId + " " + "category" + categoryId + " " + UUID.randomUUID();
        log.info(name);

        // Generate random originalPrice between 5000 and 300000
        int originalPrice = random.nextInt(300000 - 5000 + 1) + 5000;
        int salePrice = originalPrice - 1000;

        // Fixed description
        String description = "description";

        // Randomly select sex
        Sex sex = Sex.values()[random.nextInt(Sex.values().length)];

        // Sale state is fixed to ON_SALE
        SaleState saleState = SaleState.ON_SALE;

        // Generate ItemOptions
        List<ItemOptionDto> itemOptions = generateItemOptions(name);

        // Create the ItemCreateRequest object
        return ItemCreateRequest.builder()
                .name(name)
                .originalPrice(originalPrice)
                .salePrice(salePrice)
                .description(description)
                .sex(sex)
                .saleState(saleState)
                .storeId(storeId)
                .itemOptions(itemOptions)
                .categoryId(categoryId)
                .build();
    }

    // Generate a list of ItemOptionDto objects
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

