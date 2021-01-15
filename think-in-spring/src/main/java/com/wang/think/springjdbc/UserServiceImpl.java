package com.wang.think.springjdbc;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.List;

/**
 * @description: 创建数据操作接口的 实现
 * @author: wei·man cui
 * @date: 2021/1/15 17:14
 */
public class UserServiceImpl implements UserService {

    private JdbcTemplate jdbcTemplate;

    /**
     * 设置数据源
     *
     * @param dataSource 数据源
     */
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void save(User user) {
        String sql = "insert into user (username,age,sex) values(?,?,?)";
        Object[] params = {user.getUsername(), user.getAge(), user.getSex()};
        int[] types = {Types.VARCHAR, Types.INTEGER, Types.VARCHAR};
        jdbcTemplate.update(sql, params, types);
    }

    @Override
    public List<User> listUser() {
        String sql = "select * from user";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }
}
