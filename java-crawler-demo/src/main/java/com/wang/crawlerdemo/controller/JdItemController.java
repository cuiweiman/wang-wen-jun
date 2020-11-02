package com.wang.crawlerdemo.controller;

import com.wang.crawlerdemo.service.IJdItemService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 京东商品表(JdItem)表控制层
 *
 * @author makejava
 * @since 2020-11-02 10:06:23
 */
@RestController
@RequestMapping("jdItem")
public class JdItemController {
    /**
     * 服务对象
     */
    @Resource
    private IJdItemService jdItemService;



}