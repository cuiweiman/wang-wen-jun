package com.wang.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2022/6/7 17:49
 */
@RestController
@RequestMapping("/api")
public class HelloController {

    private final StringRedisTemplate redisTemplate;

    public HelloController(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/views")
    public String views() {
        Long views = redisTemplate.opsForValue().increment("views");
        return String.format("浏览量: %d \n", views);
    }

}
