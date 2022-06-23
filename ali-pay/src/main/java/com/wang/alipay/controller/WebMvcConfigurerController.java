package com.wang.alipay.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 测试 WebMvcConfigurer 类中方法的执行顺序
 * @author: cuiweiman
 * @date: 2022/6/23 10:55
 */
@RestController
@RequestMapping("/web")
public class WebMvcConfigurerController {

    @GetMapping("/test")
    public Boolean test(){
        return true;
    }

}
