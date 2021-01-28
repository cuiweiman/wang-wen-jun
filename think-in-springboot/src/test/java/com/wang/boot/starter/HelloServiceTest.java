package com.wang.boot.starter;

import com.wang.boot.MainApplicationTest;
import org.junit.Assert;
import org.junit.Test;
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
        Assert.assertEquals("hello!", helloService.sayHello());
    }

}
