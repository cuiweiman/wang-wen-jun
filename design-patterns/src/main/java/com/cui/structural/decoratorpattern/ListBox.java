package com.cui.structural.decoratorpattern;

/**
 * @description: 列表框类：具体构件类
 * @date: 2020/7/13 22:57
 * @author: wei·man cui
 */
public class ListBox extends Component {
    @Override
    public void display() {
        System.out.println("显示列表框");
    }
}
