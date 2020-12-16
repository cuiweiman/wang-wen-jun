package com.wang.think.lookupmethod;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description: 测试方法
 * @date: 2020/12/16 22:40
 * @author: wei·man cui
 */
public class Main {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("test/lookup/lookupTest.xml");
        GetBeanTest getBeanTest = (GetBeanTest) context.getBean("getBeanTest");
        getBeanTest.showMe();
    }

}
