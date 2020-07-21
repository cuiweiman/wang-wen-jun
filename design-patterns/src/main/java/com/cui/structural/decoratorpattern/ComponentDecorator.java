package com.cui.structural.decoratorpattern;

/**
 * @description: 构件装饰类：抽象装饰类
 * @date: 2020/7/13 22:57
 * @author: wei·man cui
 */
public class ComponentDecorator extends Component {
    private Component component;

    public ComponentDecorator(Component component) {
        this.component = component;
    }

    @Override
    public void display() {
        component.display();
    }
}
