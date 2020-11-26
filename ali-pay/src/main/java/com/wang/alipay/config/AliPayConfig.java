package com.wang.alipay.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.wang.alipay.properties.AliPayProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @description: 阿里支付 配置信息 aliPay-sdk-java
 * @author: wei·man cui
 * @date: 2020/11/26 10:27
 */
@Configuration
public class AliPayConfig {

    @Resource
    AliPayProperties properties;

    @Bean
    public AlipayClient alipayClient() {
        return new DefaultAlipayClient(properties.getGatewayUrl(),
                properties.getAppId(),
                properties.getPrivateKey(),
                properties.getFormat(),
                properties.getCharset(),
                properties.getPublicKey(),
                properties.getSignType());
    }

}
