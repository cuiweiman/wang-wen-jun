package com.cui.structural.decoratorpattern;

/**
 * @description: 户端测试代码
 * @date: 2020/7/13 23:00
 * @author: wei·man cui
 */
public class Client {
    public static void main(String[] args) {
        Component component, componentSB, componentBB;
        component = new Window();
        componentSB = new BlackBorderDecorator(component);
        componentBB = new ScrollBarDecorator(componentSB);
        componentBB.display();
    }
}
