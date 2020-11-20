package com.wang.alipay.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @description: 阿里支付 沙箱
 * @author: wei·man cui
 * @date: 2020/11/20 10:54
 */
@Slf4j
@Controller
@RequestMapping("/aliPay")
public class AliPayController {

    @Value("${aliPay.appId}")
    private String appId;

    @Value("${aliPay.privateKey}")
    private String privateKey;

    @Value("${aliPay.publicKey}")
    private String publicKey;

    @Value("${aliPay.gatewayUrl}")
    private String gatewayUrl;

    @Value("${aliPay.notifyUrl}")
    private String notifyUrl;

    @Value("${aliPay.returnUrl}")
    private String returnUrl;

    private final String CHARSET = "UTF-8";

    private final String FORMAT = "JSON";

    private final String SIGN_TYPE = "RSA2";

    @ResponseBody
    @GetMapping("/hi")
    public String hi() {
        return "hi";
    }

    @ResponseBody
    @GetMapping("/pay")
    public void aliPay(HttpServletResponse response) throws IOException {
        String productCode = UUID.randomUUID().toString().replace("-", "");
        String outTradeNo = "20201120174545001";
        String totalAmount = "0.01";
        String subject = "沙箱测试订单商品名称";
        // String body = "这个商品是沙箱测试订单，这里是商品描述";

        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, appId, privateKey, FORMAT, CHARSET, publicKey, SIGN_TYPE);
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setReturnUrl(returnUrl);
        request.setNotifyUrl(notifyUrl);
        // ProductInfo productInfo = new ProductInfo(outTradeNo, totalAmount, subject, body, productCode);
        // log.info("【商品支付信息】 productInfo={} ", productInfo);
        // request.setBizContent(JSONObject.toJSONString(productInfo));

        String productInfo2 = "{"
                + "\"out_trade_no\":\"" + outTradeNo + "\","
                + "\"total_amount\":\"" + totalAmount + "\","
                + "\"subject\":\"" + subject + "\","
                // + "\"body\":\"" + body + "\","
                + "\"product_code\":\"" + productCode + "\""
                + "}";
        log.info("【商品支付信息】 productInfo={} ", productInfo2);
        request.setBizContent(productInfo2);
        String form = "";
        try {
            form = alipayClient.pageExecute(request).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        // return form;
        response.setContentType("text/html;charset=" + CHARSET);
        response.getWriter().write(form);
        response.getWriter().flush();
        response.getWriter().close();
    }

    @GetMapping("/returnUrl")
    public ModelAndView returnUrl(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, AlipayApiException {
        ModelAndView mv = new ModelAndView();
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            valueStr = new String(valueStr.getBytes("utf-8"), "utf-8");
            params.put(name, valueStr);
        }
        System.out.println(params);
        boolean signVerified = AlipaySignature.rsaCheckV1(params, publicKey, CHARSET, SIGN_TYPE);
        if (signVerified) {
            //TODO 修改订单状态

            String outTradeNo = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
            String tradeNo = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
            String totalAmount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");
            mv.addObject("outTradeNo", outTradeNo);
            mv.addObject("tradeNo", tradeNo);
            mv.addObject("totalAmount", totalAmount);
            mv.setViewName("aliPaySuccess");
        } else {
            mv.setViewName("aliPayFail");
        }
        return mv;
    }

    @GetMapping("/notifyUrl")
    public ModelAndView notifyUrl(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("notify");
        return mv;
    }

}
