package com.wang.think.replacedmethod;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description: 测试方法
 * @date: 2020/12/16 22:40
 * @author: wei·man cui
 */
public class Main {

    public static void main(String[] args) {
        TestChangeMethod old = new TestChangeMethod();
        old.changeMe();

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("test/replaced/replacedMethod.xml");
        TestChangeMethod testChangeMethod = (TestChangeMethod) context.getBean("testChangeMethod");
        testChangeMethod.changeMe();
    }

}
