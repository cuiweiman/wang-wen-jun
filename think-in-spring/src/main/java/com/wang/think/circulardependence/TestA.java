package com.wang.think.circulardependence;

/**
 * @description: 循环引用类 案例
 * @author: wei·man cui
 * @date: 2020/12/23 17:53
 */
public class TestA {

    private TestB testB;

    public TestA(TestB testB) {
        this.testB = testB;
    }

    public void a(){
        testB.b();
    }

    public TestB getTestB() {
        return testB;
    }

    public void setTestB(TestB testB) {
        this.testB = testB;
    }
}
