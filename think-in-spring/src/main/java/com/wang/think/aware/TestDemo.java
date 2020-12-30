package com.wang.think.aware;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description: 测试方法
 * @author: wei·man cui
 * @date: 2020/12/30 13:38
 */
public class TestDemo {

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("beanFactoryTest.xml");
        Test test = (Test) ctx.getBean("test");
        test.testAware();
    }

}
