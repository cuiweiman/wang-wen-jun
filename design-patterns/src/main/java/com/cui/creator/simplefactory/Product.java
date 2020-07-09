package com.cui.creator.simplefactory;

/**
 * @description: 简单/静态工厂模式  抽象产品类
 * @author: weiman cui
 * @date: 2020/7/9 15:04
 */
public abstract class Product {

    /**
     * 公共业务方法
     */
    public void commonMethod() {
        System.out.println("Common");
    }

    /**
     * 声明 抽象业务 方法
     */
    public void methodDiff() {

    }


}
