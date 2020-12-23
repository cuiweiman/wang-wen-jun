package com.wang.think.circulardependence;

/**
 * @description: 循环引用类 案例
 * @author: wei·man cui
 * @date: 2020/12/23 17:53
 */
public class TestB {

    private TestC testC;

    public TestB() {
    }

    /**
     * 构造器 循环依赖，无法解决
     *
     * @param testC 参数
     */
    public TestB(TestC testC) {
        this.testC = testC;
    }

    /**
     * setter 方式注入的循环依赖，可以解决（提前暴露了 ObjectFactory）
     */
    public void b() {
        testC.c();
    }

    public TestC getTestC() {
        return testC;
    }

    public void setTestC(TestC testC) {
        this.testC = testC;
    }
}
