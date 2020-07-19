package com.cui.behavior.command.a;

import java.util.ArrayList;

/**
 * <p>
 * 设置功能键
 * 每一个具体命令类对应一个请求的处理者（接收者），
 * 通过向请求发送者注入不同的具体命令对象可以使得相同的发送者对应不同的接收者，
 * 从而实现“将一个请求封装为一个对象，用不同的请求对客户进行参数化”，
 * 客户端只需要将具体命令对象作为参数注入请求发送者，无须直接操作请求的接收者。
 * </p>
 *
 * @description: 功能键设置窗口类
 * @date: 2020/7/15 22:24
 * @author: weiman cui
 */
public class FBSettingWindow {
    // 窗口标题
    private String title;

    private ArrayList<FunctionButton> functionButtons = new ArrayList<>();

    public FBSettingWindow(String title) {
        this.title = title;
    }

    public void addFunctionButton(FunctionButton functionButton) {
        functionButtons.add(functionButton);
    }

    public void removeFunctionButton(FunctionButton functionButton) {
        functionButtons.remove(functionButton);
    }

    // 显示窗口以及功能键
    public void display() {
        System.out.println("显示窗口：" + this.title);
        System.out.println("显示功能键：");
        for (Object obj : functionButtons) {
            System.out.println(((FunctionButton) obj).getName());
        }
        System.out.println("------------------------------");
    }

}

/**
 * @description: 功能键类：请求发送者
 * @author: weiman cui
 * @date: 2020/7/15 22:27
 */
class FunctionButton {
    // 功能键名称
    private String name;

    // 维持一个抽象命令对象的引用
    private Command command;

    public FunctionButton(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    //发送请求的方法
    public void onClick() {
        System.out.print("点击功能键：");
        command.execute();
    }
}

/**
 * @description: 抽象命令类
 * @author: weiman cui
 * @date: 2020/7/15 22:28
 */
abstract class Command {
    public abstract void execute();
}

/**
 * @description: 帮助命令类：具体命令类
 * @author: weiman cui
 * @date: 2020/7/15 22:29
 */
class HelpCommand extends Command {
    //维持对请求接收者的引用
    private HelpHandler hhObj;

    public HelpCommand() {
        hhObj = new HelpHandler();
    }

    //命令执行方法，将调用请求接收者的业务方法
    public void execute() {
        hhObj.display();
    }
}

/**
 * @description: 最小化命令类：具体命令类
 * @author: weiman cui
 * @date: 2020/7/15 22:29
 */
class MinimizeCommand extends Command {
    //维持对请求接收者的引用
    private WindowHanlder whObj;

    public MinimizeCommand() {
        whObj = new WindowHanlder();
    }

    //命令执行方法，将调用请求接收者的业务方法
    public void execute() {
        whObj.minimize();
    }
}

/**
 * @description: 窗口处理类：请求接收者
 * @author: weiman cui
 * @date: 2020/7/15 22:29
 */
class WindowHanlder {
    public void minimize() {
        System.out.println("将窗口最小化至托盘！");
    }
}

/**
 * @description: 帮助文档处理类：请求接收者
 * @author: weiman cui
 * @date: 2020/7/15 22:29
 */
class HelpHandler {
    public void display() {
        System.out.println("显示帮助文档！");
    }
}

