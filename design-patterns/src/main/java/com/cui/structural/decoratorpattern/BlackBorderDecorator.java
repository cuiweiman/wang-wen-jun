package com.cui.structural.decoratorpattern;

/**
 * @description: 黑色边框装饰类：具体装饰类
 * @date: 2020/7/13 23:00
 * @author: wei·man cui
 */
public class BlackBorderDecorator extends ComponentDecorator {

    public BlackBorderDecorator(Component component) {
        super(component);
    }

    @Override
    public void display() {
        this.setBlackBorder();
        super.display();
    }

    public void setBlackBorder() {
        System.out.println("设置黑色边框");
    }
}
