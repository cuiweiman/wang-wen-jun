package com.cui.creator.abstractfactory;

import com.cui.creator.simplefactory.ConCreateProductA;

/**
 * @description: 具体工厂类 实现抽象工厂类
 * @author: weiman cui
 * @date: 2020/7/9 16:09
 */
public class ConCreateFactory1 extends AbstractFactory {
    @Override
    public AbstractProductA createProductA() {
        return new AbProductA();
    }

    @Override
    public AbstractProductB createProductB() {
        return new AbProductB();
    }
}
