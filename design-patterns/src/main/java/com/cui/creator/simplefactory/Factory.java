package com.cui.creator.simplefactory;

/**
 * @description: 简单/静态工厂模式  静态工厂类
 * @author: weiman cui
 * @date: 2020/7/9 15:07
 */
public class Factory {
    public static Product getProduct(String arg) {
        Product product = null;
        if (arg.equalsIgnoreCase("A")) {
            product = new ConCreateProductA();
        } else if (arg.equalsIgnoreCase("B")) {
            product = new ConCreateProductB();
        }
        return product;
    }
}
