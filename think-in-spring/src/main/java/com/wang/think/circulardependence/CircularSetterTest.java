package com.wang.think.circulardependence;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description: 启动测试——setter注入 依赖循环，可以被Spring容器处理（提前暴露创建中 bean 的 ObjectFactory）
 * @date: 2020/12/23 23:24
 * @author: wei·man cui
 */
public class CircularSetterTest {
    public static void main(String[] args) throws Throwable {
        try {
            new ClassPathXmlApplicationContext("test/circulardependence/circular-setter.xml");
        } catch (Exception e) {
            // 会在 创建 TestC 时抛出
            Throwable e1 = e.getCause().getCause().getCause();
            throw e1;
        }

    }
}
