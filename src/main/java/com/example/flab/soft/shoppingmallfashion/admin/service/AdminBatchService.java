package com.example.flab.soft.shoppingmallfashion.admin.service;

import com.example.flab.soft.shoppingmallfashion.admin.dto.CreatedDataInfo;
import com.example.flab.soft.shoppingmallfashion.admin.dto.IdNameDto;
import com.example.flab.soft.shoppingmallfashion.admin.dto.TestItemDto;
import com.example.flab.soft.shoppingmallfashion.admin.util.IdNameRowMapper;
import com.example.flab.soft.shoppingmallfashion.admin.util.ItemDtoGenerator;
import com.example.flab.soft.shoppingmallfashion.item.controller.ItemOptionDto;
import com.example.flab.soft.shoppingmallfashion.item.domain.SaleState;
import com.example.flab.soft.shoppingmallfashion.store.repository.Store;
import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminBatchService {
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

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

        Map<String, Long> nameIdMap = new HashMap<>((int) (idNameDtos.size() * 1.4));
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

    public void bulkInsertItems(Integer itemCount, CreatedDataInfo userCreatedDataInfo,
                                CreatedDataInfo storeCreatedDataInfo,
                                CreatedDataInfo categoryCreatedDataInfo) {
        String itemSql = "INSERT INTO items "
                + "(name, original_price, sale_price, description, "
                + "sex, sale_state, store_id, category_id, lastly_modified_by, order_count) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String itemOptionSql = "INSERT INTO item_options "
                + "(name, size, item_id, sale_state, stocks_count) "
                + "VALUES (?, ?, ?, ?, ?)";

        int groupSize = 200;
        int numberOfItemGroups = (int) Math.ceil((double) itemCount / groupSize);

        long before = System.currentTimeMillis();

        IntStream.range(0, numberOfItemGroups)
                .parallel()
                .mapToObj(group -> IntStream.range(0, groupSize)
                        .mapToObj(i -> ItemDtoGenerator.generateItemTestDtos(userCreatedDataInfo,
                                storeCreatedDataInfo, categoryCreatedDataInfo))
                        .toList())
                .forEach(itemGroup -> {
                    KeyHolder keyHolder = new GeneratedKeyHolder();
                    PreparedStatementCreator psc = connection ->
                            connection.prepareStatement(itemSql, Statement.RETURN_GENERATED_KEYS);
                    jdbcTemplate.batchUpdate(psc, new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            TestItemDto item = itemGroup.get(i);
                            ps.setString(1, item.getName());
                            ps.setInt(2, item.getOriginalPrice());
                            ps.setInt(3, item.getSalePrice());
                            ps.setString(4, item.getDescription());
                            ps.setString(5, item.getSex().name());
                            ps.setString(6, item.getSaleState().name());
                            ps.setLong(7, item.getStoreId());
                            ps.setLong(8, item.getCategoryId());
                            ps.setLong(9, item.getIsModifiedBy());
                            ps.setInt(10, item.getOrderCount());
                        }

                        @Override
                        public int getBatchSize() {
                            return itemGroup.size();
                        }
                    }, keyHolder);

                    List<Map<String, Object>> generatedKeys = keyHolder.getKeyList();

                    List<Object[]> optionBatchArgs = new ArrayList<>();
                    for (int i = 0; i < generatedKeys.size(); i++) {
                        Long generatedItemId = ((Number) generatedKeys.get(i).get("GENERATED_KEY")).longValue();
                        TestItemDto item = itemGroup.get(i);
                        List<ItemOptionDto> options = item.getItemOptions();

                        for (ItemOptionDto option : options) {
                            optionBatchArgs.add(new Object[]{
                                    option.getName(),
                                    option.getSize(),
                                    generatedItemId,
                                    option.getSaleState().name(),
                                    option.getStocksCount()
                            });
                        }
                    }

                    jdbcTemplate.batchUpdate(itemOptionSql, optionBatchArgs);
                });

        log.info("아이템 배치에 걸린 시간: {}", System.currentTimeMillis() - before);
    }

    public void encryptPassword() {
        String testPassword = "Password1#";
        String encodedPassword = passwordEncoder.encode(testPassword);

        String sql = "UPDATE users SET password = ?";

        jdbcTemplate.update(sql, encodedPassword);
    }
}