package com.wang.alipay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description: 页面导向
 * @author: wei·man cui
 * @date: 2021/1/13 11:21
 */
@Controller
@RequestMapping("/page")
public class ModelController {

    @GetMapping("/toPayIframe")
    public String toPayIframe() {
        return "toPayIframe";
    }

}
