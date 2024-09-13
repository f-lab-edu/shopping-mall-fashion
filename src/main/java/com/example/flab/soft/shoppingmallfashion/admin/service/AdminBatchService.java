package com.example.flab.soft.shoppingmallfashion.admin.service;

import com.example.flab.soft.shoppingmallfashion.admin.dto.ItemIdNameDto;
import com.example.flab.soft.shoppingmallfashion.admin.dto.TestItemDto;
import com.example.flab.soft.shoppingmallfashion.admin.util.ItemRowMapper;
import com.example.flab.soft.shoppingmallfashion.item.controller.ItemOptionDto;
import com.example.flab.soft.shoppingmallfashion.item.domain.SaleState;
import com.example.flab.soft.shoppingmallfashion.store.repository.Store;
import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminBatchService {
    private final JdbcTemplate jdbcTemplate;

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

        Map<String, List<ItemOptionDto>> itemNameOptionsMap =
                new HashMap<>(testItemDtos.size() * 2);

        int groupSize = 500;
        int numberOfItemGroups = (int) Math.ceil((double) testItemDtos.size() / groupSize);

        long before = System.currentTimeMillis();

        IntStream.range(0, numberOfItemGroups)
                .mapToObj(i -> testItemDtos.subList(i * groupSize, Math.min((i + 1) * groupSize, testItemDtos.size())))
                .parallel()
                .forEach(itemGroup -> {
                    List<Object[]> batchArgs = new ArrayList<>();
                    for (TestItemDto item : itemGroup) {
                        batchArgs.add(new Object[]{
                                item.getName(),
                                item.getOriginalPrice(),
                                item.getSalePrice(),
                                item.getDescription(),
                                item.getSex().name(),
                                item.getSaleState().name(),
                                item.getStoreId(),
                                item.getCategoryId(),
                                item.getIsModifiedBy(),
                                item.getOrderCount()
                        });
                        itemNameOptionsMap.put(item.getName(), item.getItemOptions());
                    }
                    jdbcTemplate.batchUpdate(itemSql, batchArgs);
                });

        log.info("아이템 배치에 걸린 시간: {}", System.currentTimeMillis() - before);

        String findAllItemsSql = "SELECT id, name FROM items";

        before = System.currentTimeMillis();

        List<ItemIdNameDto> idNameDtos = jdbcTemplate.query(findAllItemsSql, new ItemRowMapper());

        log.info("전체 아이템 조회에 걸린 시간: {}", System.currentTimeMillis() - before);

        before = System.currentTimeMillis();

        int itemOptionGroupSize = 100;
        int numberOfGrops = (int) Math.ceil((double) testItemDtos.size() / itemOptionGroupSize);

        IntStream.range(0, numberOfGrops)
                .mapToObj(i -> idNameDtos.subList(i * itemOptionGroupSize, Math.min((i + 1) * itemOptionGroupSize, idNameDtos.size())))
                .parallel()
                .forEach(itemGroup -> {
                    List<Object[]> batchArgs = new ArrayList<>();
                    for (ItemIdNameDto idNameDto : itemGroup) {
                        List<ItemOptionDto> itemOptions = itemNameOptionsMap.get(idNameDto.getName());
                        itemOptions.forEach(itemOption -> batchArgs.add(new Object[]{
                                itemOption.getName(),
                                itemOption.getSize(),
                                idNameDto.getId(),
                                itemOption.getSaleState().name(),
                                itemOption.getStocksCount()
                        }));
                    }
                    jdbcTemplate.batchUpdate(itemOptionSql, batchArgs);
                });
        log.info("아이템 옵션 배치에 걸린 시간: {}", System.currentTimeMillis() - before);
    }
}