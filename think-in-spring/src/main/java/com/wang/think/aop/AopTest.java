package com.wang.think.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description: 动态aop  使用示例
 * @date: 2021/1/10 23:29
 * @author: wei·man cui
 */
public class AopTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("test/aop/aop-test.xml");
        TestBean testBean = (TestBean) context.getBean("test");
        testBean.test();
    }
}
