package com.wang.webmagic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @description: WebMagic 框架实例,爬取Boss直聘中Java职位的数据
 * @author: wei·man cui
 * @date: 2020/11/2 11:12
 */
@EnableScheduling
@SpringBootApplication
public class WebMagicApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebMagicApplication.class, args);
        System.out.println("WebMagic 框架实例,爬取Boss直聘中Java职位的数据，项目启动成功……");
    }
}
