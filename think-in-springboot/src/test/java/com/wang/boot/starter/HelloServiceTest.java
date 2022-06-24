package com.wang.boot.starter;

import com.wang.boot.MainApplicationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @description:
 * @author: wei·man cui
 * @date: 2021/1/28 17:45
 */
public class HelloServiceTest extends MainApplicationTest {

    @Autowired
    private HelloService helloService;

    @Test
    public void sayHello() {
        System.out.println(helloService.sayHello());
        Assertions.assertEquals("hello!", helloService.sayHello());
    }

}
