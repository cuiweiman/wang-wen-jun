package com.wang.think.aop;

/**
 * @description: 动态aop  使用示例
 * @date: 2021/1/10 23:18
 * @author: wei·man cui
 */
public class TestBean {
    private String testStr = "testStr";

    public String getTestStr() {
        return testStr;
    }

    public void setTestStr(String testStr) {
        this.testStr = testStr;
    }

    public void test() {
        System.out.println(testStr);
    }

}
