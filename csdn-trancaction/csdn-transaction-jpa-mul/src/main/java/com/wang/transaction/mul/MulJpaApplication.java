package com.wang.transaction.mul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description: 多数据源 事务模块 启动类
 * @author: weiman cui
 * @date: 2020/7/3 10:18
 */
@SpringBootApplication
public class MulJpaApplication {
    public static void main(String[] args) {
        SpringApplication.run(MulJpaApplication.class, args);
    }

}
