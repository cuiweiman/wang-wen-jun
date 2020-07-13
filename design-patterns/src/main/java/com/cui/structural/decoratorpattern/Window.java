package com.cui.structural.decoratorpattern;

/**
 * @description: 窗体类：具体构件类
 * @date: 2020/7/13 22:56
 * @author: weiman cui
 */
public class Window extends Component {
    @Override
    public void display() {
        System.out.println("显示窗体");
    }
}
