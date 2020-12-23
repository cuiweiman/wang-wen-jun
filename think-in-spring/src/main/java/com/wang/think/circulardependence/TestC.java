package com.wang.think.circulardependence;

/**
 * @description: 循环应用类 案例
 * @author: wei·man cui
 * @date: 2020/12/23 17:54
 */
public class TestC {

    private TestA testA;

    public TestC(TestA testA) {
        this.testA = testA;
    }

    public void c() {
        testA.a();
    }

    public TestA getTestA() {
        return testA;
    }

    public void setTestA(TestA testA) {
        this.testA = testA;
    }
}
