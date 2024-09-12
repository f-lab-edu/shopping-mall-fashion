package com.example.flab.soft.shoppingmallfashion.admin;

import com.example.flab.soft.shoppingmallfashion.item.repository.ItemOptionRepository;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemRepository;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemSearchKeywordRepository;
import com.example.flab.soft.shoppingmallfashion.item.service.ItemBriefDto;
import com.example.flab.soft.shoppingmallfashion.item.service.ItemCommandService;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemTestDataManageService {
    private final ItemCommandService itemCommandService;
    private final ItemRepository itemRepository;
    private final ItemOptionRepository itemOptionRepository;
    private final ExecutorService executorService;
    private final TransactionTemplate txTemplate;
    private final JdbcTemplate jdbcTemplate;

    public ItemCountsDto createTestItems(Integer itemCount,
                                         CreatedDataInfo userCreatedDataInfo,
                                         CreatedDataInfo storeCreatedDataInfo,
                                         CreatedDataInfo categoryCreatedDataInfo) {
        Random random = new Random(itemCount);
        ConcurrentUtil.collect(IntStream.range(0, itemCount)
                .mapToObj(i -> {
                    long userId = random.nextLong(userCreatedDataInfo.getCreatedCount()) + userCreatedDataInfo.getFirstElementId();
                    return executorService.submit(() ->
                            txTemplate.executeWithoutResult(status -> {
                                ItemBriefDto itemBriefDto = itemCommandService.addItem(
                                        ItemRequestGenerator.generateItemCreateRequest(storeCreatedDataInfo, categoryCreatedDataInfo), userId);
                                itemCommandService.modifyOrderCount(itemBriefDto.getItemId(), random.nextLong(1000000));
                            })
                    );
                })
                .toList());
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
