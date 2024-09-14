package com.example.flab.soft.shoppingmallfashion.admin.util;

import com.example.flab.soft.shoppingmallfashion.admin.dto.IdNameDto;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class IdNameRowMapper implements RowMapper<IdNameDto> {
    @Override
    public IdNameDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        IdNameDto item = new IdNameDto();
        item.setId(rs.getLong("id"));
        item.setName(rs.getString("name"));
        return item;
    }
}