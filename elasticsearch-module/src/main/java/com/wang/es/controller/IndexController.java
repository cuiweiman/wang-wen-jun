package com.wang.es.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @description: thymeleaf 导向
 * @date: 2020/11/26 22:28
 * @author: wei·man cui
 */
@Controller
public class IndexController {

    @GetMapping("/index")
    public String index() {
        return "index";
    }

}
