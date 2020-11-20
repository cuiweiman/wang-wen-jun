package com.wang.alipay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

 /**
 * @description: 阿里支付沙箱环境启动类
 * @author: wei·man cui
 * @date: 2020/11/20 10:45
 */
@SpringBootApplication
public class AliPayApplication {
    public static void main(String[] args) {
        SpringApplication.run(AliPayApplication.class, args);
        System.out.println(" 阿里支付沙箱环境启动类 启动成功 ");
    }
}
