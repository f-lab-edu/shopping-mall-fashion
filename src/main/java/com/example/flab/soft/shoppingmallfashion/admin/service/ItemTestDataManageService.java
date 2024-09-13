package com.example.flab.soft.shoppingmallfashion.admin.service;

import com.example.flab.soft.shoppingmallfashion.admin.dto.CreatedDataInfo;
import com.example.flab.soft.shoppingmallfashion.admin.dto.ItemCountsDto;
import com.example.flab.soft.shoppingmallfashion.admin.dto.TestItemDto;
import com.example.flab.soft.shoppingmallfashion.admin.util.ConcurrentUtil;
import com.example.flab.soft.shoppingmallfashion.admin.util.ItemDtoGenerator;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemOptionRepository;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemRepository;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemTestDataManageService {
    private final ItemRepository itemRepository;
    private final ItemOptionRepository itemOptionRepository;
    private final ExecutorService executorService;
    private final JdbcTemplate jdbcTemplate;
    private final AdminBatchService adminBatchService;

    public ItemCountsDto createTestItems(Integer itemCount,
                                         CreatedDataInfo userCreatedDataInfo,
                                         CreatedDataInfo storeCreatedDataInfo,
                                         CreatedDataInfo categoryCreatedDataInfo) {
        List<TestItemDto> itemDtos = ConcurrentUtil.collect(IntStream.range(0, itemCount)
                .mapToObj(i -> {
                    return executorService.submit(() ->
                            ItemDtoGenerator.generateItemTestDtos(userCreatedDataInfo,
                                    storeCreatedDataInfo, categoryCreatedDataInfo));
                })
                .toList());
        adminBatchService.bulkInsertItems(itemDtos);
        return ItemCountsDto.builder()
                .itemCount(itemRepository.count())
                .itemOptionCount(itemOptionRepository.count())
                .build();
    }

    @Transactional
    public ItemCountsDto clearAll() {
        jdbcTemplate.execute("DELETE FROM item_search_keywords");
        jdbcTemplate.execute("DELETE FROM item_options");
        jdbcTemplate.execute("DELETE FROM items");
        return ItemCountsDto.builder()
                .itemCount(itemRepository.count())
                .itemOptionCount(itemOptionRepository.count())
                .build();
    }
}
