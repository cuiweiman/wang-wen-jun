package com.wang.think.circulardependence;

/**
 * @description: 循环引用类 案例
 * @author: wei·man cui
 * @date: 2020/12/23 17:53
 */
public class TestA {

    private TestB testB;

    public TestA() {
    }

    /**
     * 构造器 循环依赖，无法解决
     *
     * @param testB 参数
     */
    public TestA(TestB testB) {
        this.testB = testB;
    }

    /**
     * setter 方式注入的循环依赖，可以解决（提前暴露了 ObjectFactory）
     */
    public void a() {
        testB.b();
    }

    public TestB getTestB() {
        return testB;
    }

    public void setTestB(TestB testB) {
        this.testB = testB;
    }
}
