package com.wang.alipay.controller;

import com.alipay.api.AlipayClient;
import com.wang.alipay.component.AliPayComponent;
import com.wang.alipay.properties.AliPayProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * @description: 阿里支付 手机H5页面支付
 * @author: wei·man cui
 * @date: 2020/11/30 15:25
 */
@Slf4j
@Controller
@RequestMapping("/wapPay")
public class AliPayWapPayController {

    @Resource
    private AlipayClient alipayClient;

    @Resource
    private AliPayProperties aliPayProperties;

    @Resource
    private AliPayComponent aliPayComponent;













}

