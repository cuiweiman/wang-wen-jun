package com.cui.structural.flyweight;

/**
 * 围棋棋子类
 *
 * @description: 抽象享元类
 * @date: 2020/7/14 21:35
 * @author: wei·man cui
 */
public abstract class IgoChessman {

    public abstract String getColor();

    public void display() {
        System.out.println("棋子颜色：" + this.getColor());
    }

}
