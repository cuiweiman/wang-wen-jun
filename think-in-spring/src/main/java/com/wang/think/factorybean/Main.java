package com.wang.think.factorybean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description: 测试类
 * @date: 2020/12/21 22:45
 * @author: wei·man cui
 */
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("/test/factorybean/spring-car.xml");
        Car car = (Car) context.getBean("car");
        System.out.println(car.getBrand() + " " + car.getMaxSpeed() + " " + car.getPrice());
    }
}
