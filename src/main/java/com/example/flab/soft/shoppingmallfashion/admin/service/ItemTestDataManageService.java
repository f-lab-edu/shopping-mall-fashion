package com.example.flab.soft.shoppingmallfashion.admin.service;

import com.example.flab.soft.shoppingmallfashion.admin.dto.CreatedDataInfo;
import com.example.flab.soft.shoppingmallfashion.admin.dto.TestItemDto;
import com.example.flab.soft.shoppingmallfashion.admin.util.ItemDtoGenerator;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemOptionRepository;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemRepository;
import java.util.List;
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
    private final JdbcTemplate jdbcTemplate;
    private final AdminBatchService adminBatchService;

    public void createTestItems(Integer itemCount,
                                         CreatedDataInfo userCreatedDataInfo,
                                         CreatedDataInfo storeCreatedDataInfo,
                                         CreatedDataInfo categoryCreatedDataInfo) {
        adminBatchService.bulkInsertItems(itemCount, userCreatedDataInfo, storeCreatedDataInfo, categoryCreatedDataInfo);
    }

    @Transactional
    public void clearAll() {
        jdbcTemplate.execute("DELETE FROM item_search_keywords");
        jdbcTemplate.execute("DELETE FROM item_options");
        jdbcTemplate.execute("DELETE FROM items");
    }

    public Long countItems() {
        return itemRepository.count();
    }

    public Long countItemOptions() {
        return itemOptionRepository.count();
    }
}
