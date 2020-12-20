package com.wang.think.customeralias;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.Resource;

/**
 * @description: 测试类
 * @date: 2020/12/20 23:52
 * @author: wei·man cui
 */
public class Main {
    public static void main(String[] args) {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("test/customeralias/customer.xml");
        User user = (User) context.getBean("testBean");
        System.out.println(user.getUserName() + " " + user.getEmail());
    }
}
