package com.wang.nettyboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: wei·man cui
 * @date: 2021/4/30 9:56
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class WebApiController {

    @GetMapping("/test")
    public Object test() {
        log.info("Web Api Test");
        return "success";
    }

}
