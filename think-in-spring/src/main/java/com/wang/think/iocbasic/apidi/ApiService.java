package com.wang.think.iocbasic.apidi;

/**
 * @description: 通过 Java Configuration注解的配置，将bean注入Spring的bean工厂:service
 * @author: wei·man cui
 * @date: 2021/1/29 17:52
 */
public class ApiService {

    public void sayHi() {
        System.out.println("通过 Java Configuration注解的配置，将 bean 注入 Spring 的 bean 工厂");
    }
}
