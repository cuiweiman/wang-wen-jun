package com.wang.think.circulardependence;

/**
 * @description: 循环应用类 案例
 * @author: wei·man cui
 * @date: 2020/12/23 17:54
 */
public class TestC {

    private TestA testA;

    public TestC() {
    }

    /**
     * 构造器 循环依赖，无法解决
     *
     * @param testA 参数
     */
    public TestC(TestA testA) {
        this.testA = testA;
    }

    /**
     * setter 方式注入的循环依赖，可以解决（提前暴露了 ObjectFactory）
     */
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
