package com.example.flab.soft.shoppingmallfashion.admin.service;

import com.example.flab.soft.shoppingmallfashion.admin.dto.CreatedDataInfo;
import com.example.flab.soft.shoppingmallfashion.admin.util.StoreGenerator;
import com.example.flab.soft.shoppingmallfashion.item.domain.SaleState;
import com.example.flab.soft.shoppingmallfashion.store.repository.Store;
import com.example.flab.soft.shoppingmallfashion.store.repository.StoreRepository;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreTestDataManageService {
    private final StoreRepository storeRepository;
    private final JdbcTemplate jdbcTemplate;
    private final AdminBatchService adminBatchService;

    public CreatedDataInfo createTestStores(Integer count, CreatedDataInfo createdUserDataInfo) {
        Random random = new Random(createdUserDataInfo.getCreatedCount());
        List<Store> stores = IntStream.range(0, count)
                .mapToObj(storeId -> generateStore(createdUserDataInfo, storeId, random))
                .toList();
        adminBatchService.bulkInsertStores(stores, SaleState.ON_SALE);
        return CreatedDataInfo.builder()
                .createdCount(storeRepository.count())
                .firstElementId(storeRepository.findFirstBy().getId())
                .build();
    }

    private Store generateStore(CreatedDataInfo createdUserDataInfo, int storeId, Random random) {
        long userId = random.nextLong(createdUserDataInfo.getCreatedCount())
                + createdUserDataInfo.getFirstElementId();
        return StoreGenerator.generateStore(storeId, userId);
    }

    @Transactional
    public void clearAll() {
        jdbcTemplate.execute("DELETE FROM stores");
    }

    public Long count() {
        return storeRepository.count();
    }
}
