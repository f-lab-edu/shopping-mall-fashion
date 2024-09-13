package com.example.flab.soft.shoppingmallfashion.admin;

import com.example.flab.soft.shoppingmallfashion.item.controller.ItemOptionDto;
import com.example.flab.soft.shoppingmallfashion.item.domain.SaleState;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemRepository;
import com.example.flab.soft.shoppingmallfashion.store.repository.Store;
import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminBatchService {
    private final JdbcTemplate jdbcTemplate;
    private final ExecutorService threads;

    public void bulkInsertUsers(List<User> users) {
        String sql = "INSERT INTO users (email, password, real_name, cellphone_number, nickname) " +
                "VALUES (?, ?, ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();
        for (User user : users) {
            batchArgs.add(new Object[]{
                    user.getEmail(),
                    user.getPassword(),
                    user.getRealName(),
                    user.getCellphoneNumber(),
                    user.getNickname()
            });
        }

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    public void bulkInsertStores(List<Store> stores, SaleState saleState) {
        String sql = "INSERT INTO stores "
                + "(name, logo, description, business_registration_number, manager_id, sale_state) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();
        for (Store store : stores) {
            batchArgs.add(new Object[]{
                    store.getName(),
                    store.getLogo(),
                    store.getDescription(),
                    store.getBusinessRegistrationNumber(),
                    store.getManagerId(),
                    saleState.name()
            });
        }

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    public void bulkInsertItems(List<TestItemDto> testItemDtos) {
        String itemSql = "INSERT INTO items "
                + "(name, original_price, sale_price, description, "
                + "sex, sale_state, store_id, category_id, lastly_modified_by, order_count)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String itemOptionSql = "INSERT INTO item_options "
                + "(name, size, item_id, sale_state, stocks_count) "
                + "VALUES (?, ?, ?, ?, ?)";

        List<Object[]> itemBatchArgs = new ArrayList<>();
        Map<String, List<ItemOptionDto>> itemNameOptionsMap =
                new HashMap<>(testItemDtos.size() * 2);
        for (TestItemDto testItemDto : testItemDtos) {
            itemBatchArgs.add(new Object[]{
                    testItemDto.getName(),
                    testItemDto.getOriginalPrice(),
                    testItemDto.getSalePrice(),
                    testItemDto.getDescription(),
                    testItemDto.getSex().name(),
                    testItemDto.getSaleState().name(),
                    testItemDto.getStoreId(),
                    testItemDto.getCategoryId(),
                    testItemDto.getIsModifiedBy(),
                    testItemDto.getOrderCount()
            });
            itemNameOptionsMap.put(testItemDto.getName(), testItemDto.getItemOptions());
        }
        jdbcTemplate.batchUpdate(itemSql, itemBatchArgs);

        String findAllItemsSql = "SELECT id, name FROM items";

        List<ItemIdNameDto> idNameDtos = jdbcTemplate.query(findAllItemsSql, new ItemRowMapper());
        ConcurrentLinkedQueue<Object[]> itemOptionBatchArgs = new ConcurrentLinkedQueue<>();

        CountDownLatch latch = new CountDownLatch(idNameDtos.size());

        for (ItemIdNameDto idNameDto : idNameDtos) {
            threads.submit(() -> {
                try {
                    List<ItemOptionDto> itemOptions = itemNameOptionsMap.get(idNameDto.getName());
                    itemOptions.forEach(itemOption -> itemOptionBatchArgs.add(new Object[]{
                            itemOption.getName(),
                            itemOption.getSize(),
                            idNameDto.getId(),
                            itemOption.getSaleState().name(),
                            itemOption.getStocksCount()
                    }));
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
            jdbcTemplate.batchUpdate(itemOptionSql, new ArrayList<>(itemOptionBatchArgs));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}