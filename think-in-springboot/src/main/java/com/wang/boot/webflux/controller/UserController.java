package com.wang.boot.webflux.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

/**
 * Mono/Flux 流式编程
 *
 * @description: 用户 处理器：即 控制器
 * @date: 2021/4/17 16:54
 * @author: wei·man cui
 */
@Slf4j
@RestController
public class UserController {

    @GetMapping("tradition")
    public String getTradition() {
        long start = System.currentTimeMillis();
        log.info("tradition start");
        String res = this.createStr("tradition");
        log.info("tradition end. 耗时：{}", System.currentTimeMillis() - start);
        return res;
    }

    @GetMapping("mono")
    public Mono<String> monoTest() {
        long start = System.currentTimeMillis();
        log.info("mono start");
        Mono<String> res = Mono.fromSupplier(() -> this.createStr("mono"));
        log.info("mono end. 耗时：{}", System.currentTimeMillis() - start);
        return res;
    }

    private String createStr(String str) {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException ignored) {
        }
        return str.concat(" some string");
    }

}
