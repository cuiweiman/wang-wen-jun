package com.cui.structural.command.c;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 简易计算器，
 * 该计算器可以实现简单的数学运算，
 * 还可以对运算实施撤销操作。
 * </p>
 *
 * @description: 简易计算器 加法操作
 * @author: weiman cui
 * @date: 2020/7/16
 */
public class Adder {
    private int num = 0;

    public int add(int value) {
        return num += value;
    }
}

/**
 * @description: 抽象命令类
 * @author: weiman cui
 * @date: 2020/7/16
 */
abstract class AbstractCommand {
    // 声明方法执行命令
    public abstract int execute(int value);

    // 声明方法撤销操作
    public abstract int undo();
}


/**
 * @description: 具体命令类
 * @author: weiman cui
 * @date: 2020/7/16
 */
class ConCreateCommand extends AbstractCommand {
    private Adder adder = new Adder();
    private int value;

    private List<Integer> historyValues = new ArrayList<>();

    @Override
    public int execute(int value) {
        this.value = value;
        historyValues.add(value);
        return adder.add(value);
    }

    @Override
    public int undo() {
        if (historyValues.size() > 0) {
            int index = historyValues.size() - 1;
            int historyValue = historyValues.get(index);
            historyValues.remove(index);
            return adder.add(-historyValue);
        }
        return 0;
    }
}

/**
 * @description: 计算机界面类：请求发送者
 * @author: weiman cui
 * @date: 2020/7/16
 */
class CalculatorForm {
    private AbstractCommand command;

    public void setCommand(AbstractCommand command) {
        this.command = command;
    }

    public void compute(int value) {
        int i = command.execute(value);
        System.out.println("执行运算，运算结果为：" + i);
    }

    public void undo() {
        int i = command.undo();
        System.out.println("执行撤销，运算结果为：" + i);
    }
}






