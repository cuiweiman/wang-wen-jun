package com.wang.alipay.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: thymeleaf 向导
 * @author: wei·man cui
 * @date: 2020/11/20 11:24
 */
@RestController
@RequestMapping("/test")
public class PageController {

    @GetMapping("/hello")
    public String hi() {
        return "hi";
    }


}
