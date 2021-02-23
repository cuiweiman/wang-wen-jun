package com.wang.boot.limiting.controller;

import com.wang.boot.limiting.annotation.Limit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 接口限流Demo
 * @author: wei·man cui
 * @date: 2021/2/22 10:18
 */
@Slf4j
@RestController
@RequestMapping("/limit")
public class LimitingCurrentController {

    /**
     * 30s 允许访问 5次
     *
     * @return 结果
     */
    @Limit(name = "限流测试", key = "test", prefix = "limit", period = 30, count = 5)
    @RequestMapping("/test")
    public String limitTest() {
        return "拦截测试接口：接口访问成功";
    }


}
