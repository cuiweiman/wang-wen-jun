package com.wang.boot.starter;

import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: wei·man cui
 * @date: 2021/1/28 17:38
 */
@Component
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello() {
        return "hello!";
    }
}
