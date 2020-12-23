package com.wang.think.circulardependence;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description: 启动测试——构造器依赖循环
 * @author: wei·man cui
 * @date: 2020/12/23 18:23
 */
public class CircularConstructorTest {

    public static void main(String[] args) throws Throwable {
        try {
            new ClassPathXmlApplicationContext("test/circulardependence/circular-constructor.xml");
        } catch (Exception e) {
            // 会在 创建 TestC 时抛出
            Throwable e1 = e.getCause().getCause().getCause();
            throw e1;
        }
    }


}
