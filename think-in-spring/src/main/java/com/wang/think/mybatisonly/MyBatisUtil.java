package com.wang.think.mybatisonly;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

/**
 * @description: MyBatis的单独使用——读取配置文件，初始化 SqlSessionFactory
 * @author: wei·man cui
 * @date: 2021/1/19 9:09
 */
public class MyBatisUtil {

    private static final SqlSessionFactory sqlSessionfactory;

    static {
        String resource = "test/mybatis-only/mybatis-config.xml";
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader(resource);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        sqlSessionfactory = new SqlSessionFactoryBuilder().build(reader);
    }

    public static SqlSessionFactory getSqlSessionfactory() {
        return sqlSessionfactory;
    }
}
