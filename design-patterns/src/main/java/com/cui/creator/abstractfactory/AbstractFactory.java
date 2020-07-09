package com.cui.creator.abstractfactory;

/**
 * @description: 抽象工厂类
 * @author: weiman cui
 * @date: 2020/7/9 16:07
 */
public abstract class AbstractFactory {

    /**
     * 工厂方法一
     *
     * @return
     */
    public abstract AbstractProductA createProductA();

    /**
     * 工厂方法二
     *
     * @return
     */
    public abstract AbstractProductB createProductB();


}
