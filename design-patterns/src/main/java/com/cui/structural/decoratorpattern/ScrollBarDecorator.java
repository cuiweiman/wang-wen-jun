package com.cui.structural.decoratorpattern;

/**
 * @description: 滚动条装饰类：具体装饰类
 * @date: 2020/7/13 22:58
 * @author: weiman cui
 */
public class ScrollBarDecorator extends ComponentDecorator {

    public ScrollBarDecorator(Component component) {
        super(component);
    }

    @Override
    public void display() {
        this.setScrollBar();
        super.display();
    }

    public void setScrollBar() {
        System.out.println("增添滚动条");
    }
}
