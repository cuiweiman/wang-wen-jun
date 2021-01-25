package com.wang.think.rmi;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description: RMI 接口方法调用 测试：服务端
 * @author: wei·man cui
 * @date: 2021/1/25 16:04
 */
public class HelloRmiServer {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("test/rmi/rmi-server.xml");
    }
}
