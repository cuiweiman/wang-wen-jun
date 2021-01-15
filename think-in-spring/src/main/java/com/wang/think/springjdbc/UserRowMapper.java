package com.wang.think.springjdbc;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @description: 映射
 * @author: wei·man cui
 * @date: 2021/1/15 17:05
 */
public class UserRowMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet set, int index) throws SQLException {
        return new User(set.getLong("id"),
                set.getString("username"),
                set.getInt("age"),
                set.getString("sex"));
    }
}
