package com.example.flab.soft.shoppingmallfashion.admin;

import com.example.flab.soft.shoppingmallfashion.store.repository.Store;
import com.example.flab.soft.shoppingmallfashion.store.repository.StoreRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@RequiredArgsConstructor
public class StoreTestDataManageService {
    private final StoreRepository storeRepository;
    private final ExecutorService executorService;
    private final TransactionTemplate txTemplate;
    private final JdbcTemplate jdbcTemplate;

    public CreatedDataInfo createTestStores(Integer count, CreatedDataInfo createdUserDataInfo) {
        Random random = new Random(createdUserDataInfo.getCreatedCount());
        ConcurrentUtil.collect(IntStream.range(0, count)
                .mapToObj(storeId -> executorService.submit(() -> {
                    long userId = random.nextLong(createdUserDataInfo.getCreatedCount())
                            + createdUserDataInfo.getFirstElementId();
                    txTemplate.executeWithoutResult(status ->
                            saveAndStartSale(StoreGenerator.generateStore(storeId, userId))
                    );
                }))
                .toList());
        return CreatedDataInfo.builder()
                .createdCount(storeRepository.count())
                .firstElementId(storeRepository.findFirstBy().getId())
                .build();
    }

    private void saveAndStartSale(Store store) {
        Store savedStore = storeRepository.save(store);
        savedStore.startSale();
    }

    @Transactional
    public Long clearAll() {
        jdbcTemplate.execute("DELETE FROM stores");
        return storeRepository.count();
    }
}
