package com.wang.crawlerdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @description: 京东爬虫案例
 * @author: wei·man cui
 * @date: 2020/11/2 9:57
 * @EnableScheduling： 开启 定时任务
 */
@EnableScheduling
@SpringBootApplication
public class CrawlerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CrawlerApplication.class, args);
    }
}
