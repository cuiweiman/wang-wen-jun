package com.wang.think.circulardependence;

/**
 * @description: 循环引用类 案例
 * @author: wei·man cui
 * @date: 2020/12/23 17:53
 */
public class TestB {

    private TestC testC;

    public TestB(TestC testC) {
        this.testC = testC;
    }

    public void b(){
        testC.c();
    }

    public TestC getTestC() {
        return testC;
    }

    public void setTestC(TestC testC) {
        this.testC = testC;
    }
}
