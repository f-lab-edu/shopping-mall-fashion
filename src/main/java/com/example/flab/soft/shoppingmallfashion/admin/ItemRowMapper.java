package com.example.flab.soft.shoppingmallfashion.admin;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class ItemRowMapper implements RowMapper<ItemIdNameDto> {
    @Override
    public ItemIdNameDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        ItemIdNameDto item = new ItemIdNameDto();
        item.setId(rs.getLong("id"));
        item.setName(rs.getString("name"));
        return item;
    }
}