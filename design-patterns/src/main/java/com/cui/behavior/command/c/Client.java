package com.cui.behavior.command.c;

/**
 * @description: 客户端测试类
 * @author: wei·man cui
 * @date: 2020/7/16
 */
public class Client {
    public static void main(String[] args) {
        CalculatorForm form = new CalculatorForm();
        AbstractCommand command = new ConCreateCommand();
        form.setCommand(command);

        form.compute(2);
        form.compute(3);
        form.compute(5);
        form.undo();
        form.undo();
        form.undo();
    }


}
