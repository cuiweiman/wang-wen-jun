package com.wang.think.circulardependence;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description: 启动测试——prototype作用域的循环依赖
 * @author: wei·man cui
 * @date: 2020/12/23 18:23
 */
public class CircularPrototypeTest {

    public static void main(String[] args) throws Throwable {
        try {
            ClassPathXmlApplicationContext context =
                    new ClassPathXmlApplicationContext("test/circulardependence/circular-prototype.xml");
            System.out.println(context.getBean("testA"));
            System.out.println(context.getBean("testB"));
            System.out.println(context.getBean("testC"));
        } catch (Exception e) {
            // 会在 创建 TestC 时抛出
            Throwable e1 = e.getCause().getCause().getCause();
            throw e1;
        }
    }


}
