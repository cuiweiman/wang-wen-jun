package com.wang.tsdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description: TSDB 时序数据库 API
 * @Author: cuiweiman
 * @Since: 2021/6/28 下午4:00
 */
@SpringBootApplication
public class TsdbApplication {
    public static void main(String[] args) {
        SpringApplication.run(TsdbApplication.class, args);
    }
}
