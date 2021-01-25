package com.wang.think.rmi;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description: RMI 接口方法调用 测试：客户端
 * @author: wei·man cui
 * @date: 2021/1/25 16:09
 */
public class HelloRmiCilent {
    public static void main(String[] args) {
        final ApplicationContext applicationContext = new ClassPathXmlApplicationContext("test/rmi/rmi-client.xml");
        final HelloRmiService helloRmiService = applicationContext.getBean("myClient", HelloRmiService.class);
        System.out.println(helloRmiService.getAdd(1, 2));
    }
}
