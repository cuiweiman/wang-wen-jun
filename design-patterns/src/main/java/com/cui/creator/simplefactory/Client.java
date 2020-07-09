package com.cui.creator.simplefactory;

/**
 * @description: 简单/静态工厂模式 主方法
 * @author: weiman cui
 * @date: 2020/7/9 15:09
 */
public class Client {
    public static void main(String[] args) {
        Product product = Factory.getProduct("B");
        product.commonMethod();
        product.methodDiff();
    }
}
