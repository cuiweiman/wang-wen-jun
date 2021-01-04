package com.wang.alipay.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.wang.alipay.properties.AliPayProperties;
import com.wang.alipay.utils.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    @GetMapping("/toPay")
    public void aliPayWepPay(HttpServletResponse response) throws AlipayApiException, IOException {
        final AlipayTradeWapPayModel wapPayModel = new AlipayTradeWapPayModel();
        final String seriesNum = IdGenerator.seriesNum();
        log.info("商户订单号= {}", seriesNum);
        wapPayModel.setOutTradeNo(seriesNum);
        wapPayModel.setSubject("手机H5支付");
        wapPayModel.setTotalAmount("5.00");
        wapPayModel.setBody("支付测试，共5.00元");
        wapPayModel.setTimeoutExpress("1m");
        wapPayModel.setProductCode("QUICK_WAP_WAY");

        final AlipayTradeWapPayRequest wapPayRequest = new AlipayTradeWapPayRequest();
        wapPayRequest.setNotifyUrl(aliPayProperties.getNotifyUrl());
        wapPayRequest.setReturnUrl(aliPayProperties.getReturnUrl());
        wapPayRequest.setBizModel(wapPayModel);

        final String form = alipayClient.pageExecute(wapPayRequest).getBody();
        response.setContentType("text/html;charset=" + aliPayProperties.getCharset());
        response.getWriter().write(form);
        response.getWriter().flush();
        response.getWriter().close();
    }


}

