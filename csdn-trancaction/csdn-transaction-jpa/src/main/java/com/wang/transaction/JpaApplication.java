package com.wang.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * @description: 事务模块 启动类
 * @author: weiman cui
 * @date: 2020/7/3 10:18
 */
@SpringBootApplication
@EntityScan("com.wang.transaction.entity")
public class JpaApplication {
    public static void main(String[] args) {
        SpringApplication.run(JpaApplication.class, args);
    }

}
