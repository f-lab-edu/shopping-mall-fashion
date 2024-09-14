package com.example.flab.soft.shoppingmallfashion.admin.service;

import com.example.flab.soft.shoppingmallfashion.admin.dto.IdNameDto;
import com.example.flab.soft.shoppingmallfashion.admin.dto.TestItemDto;
import com.example.flab.soft.shoppingmallfashion.admin.util.IdNameRowMapper;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminBatchService {
    private final JdbcTemplate jdbcTemplate;

    public void bulkInsertCategories() {
        String largeCategorySql = "INSERT INTO large_categories (name) VALUES (?)";
        String categorySql = "INSERT INTO categories (name, large_category_id) VALUES (?, ?)";
        List<Object[]> largeCategorBatchArgs = new ArrayList<>();
        List<Object[]> categoryBatchArgs = new ArrayList<>();
        TestLargeCategory.getElementsInList().forEach(largeCategory ->
                largeCategorBatchArgs.add(new Object[]{largeCategory.name()}));

        jdbcTemplate.batchUpdate(largeCategorySql, largeCategorBatchArgs);

        String findAllLaregeCategoriesSql = "SELECT id, name FROM large_categories";

        List<IdNameDto> idNameDtos = jdbcTemplate.query(findAllLaregeCategoriesSql, new IdNameRowMapper());

        Map<String, Long> nameIdMap = new HashMap<>(idNameDtos.size() * 2);
        idNameDtos.forEach(idNameDto -> nameIdMap.put(idNameDto.getName(), idNameDto.getId()));

        TestLargeCategory.getElementsInList().forEach(testLargeCategory ->
                testLargeCategory.getSubCategories().forEach(subCategory ->
                        categoryBatchArgs.add(new Object[]{
                                subCategory.getName(),
                                nameIdMap.get(testLargeCategory.name())
                        })));
        jdbcTemplate.batchUpdate(categorySql, categoryBatchArgs);
    }

    public void bulkInsertUsers(List<User> users) {
        String sql = "INSERT INTO users (email, password, real_name, cellphone_number, nickname) " +
                "VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, users, 500,
                (ps, user) -> {
                    ps.setString(1, user.getEmail());
                    ps.setString(2, user.getPassword());
                    ps.setString(3, user.getRealName());
                    ps.setString(4, user.getCellphoneNumber());
                    ps.setString(5, user.getNickname());
                }
        );
    }

    public void bulkInsertStores(List<Store> stores, SaleState saleState) {
        String sql = "INSERT INTO stores "
                + "(name, logo, description, business_registration_number, manager_id, sale_state) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, stores, 500,
                (ps, store) -> {
                    ps.setString(1, store.getName());
                    ps.setString(2, store.getLogo());
                    ps.setString(3, store.getDescription());
                    ps.setString(4, store.getBusinessRegistrationNumber());
                    ps.setLong(5, store.getManagerId());
                    ps.setString(6, saleState.name());
                }
        );
    }

    @Async
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

        List<IdNameDto> idNameDtos = jdbcTemplate.query(findAllItemsSql, new IdNameRowMapper());

        log.info("전체 아이템 조회에 걸린 시간: {}", System.currentTimeMillis() - before);

        before = System.currentTimeMillis();

        int itemOptionGroupSize = 100;
        int numberOfGrops = (int) Math.ceil((double) testItemDtos.size() / itemOptionGroupSize);

        IntStream.range(0, numberOfGrops)
                .mapToObj(i -> idNameDtos.subList(i * itemOptionGroupSize, Math.min((i + 1) * itemOptionGroupSize, idNameDtos.size())))
                .parallel()
                .forEach(itemGroup -> {
                    List<Object[]> batchArgs = new ArrayList<>();
                    for (IdNameDto idNameDto : itemGroup) {
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