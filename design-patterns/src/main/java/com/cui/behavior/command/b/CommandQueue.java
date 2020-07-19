package com.cui.behavior.command.b;

import java.util.ArrayList;

/**
 * <p>
 * 有时候我们需要将多个请求排队，
 * 当一个请求发送者发送一个请求时，
 * 将不止一个请求接收者产生响应，
 * 这些请求接收者将逐个执行业务方法，
 * 完成对请求的处理。
 * 此时，我们可以通过命令队列来实现
 * </p>
 *
 * @description: 命令队列
 * @date: 2020/7/15 22:38
 * @author: weiman cui
 */
public class CommandQueue {
    class Command {
        public void execute() {
            System.out.println("执行了……");
        }
    }

    private ArrayList<Command> commands = new ArrayList<>();

    public void addCommand(Command command) {
        commands.add(command);
    }

    public void removeCommand(Command command) {
        commands.remove(command);
    }

    /**
     * 循环调用每一个命令对象的execute()方法
     */
    public void execute() {
        for (Object command : commands) {
            ((Command) command).execute();
        }
    }

}
