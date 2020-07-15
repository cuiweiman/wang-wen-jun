package com.cui.structural.command.b;

/**
 * @description: 求发送者类Invoker
 * @date: 2020/7/15 22:41
 * @author: weiman cui
 */
public class Invoker {
    // 维持一个CommandQueue对象的引用
    private CommandQueue commandQueue;

    //构造注入
    public Invoker(CommandQueue commandQueue) {
        this.commandQueue = commandQueue;
    }

    //设值注入
    public void setCommandQueue(CommandQueue commandQueue) {
        this.commandQueue = commandQueue;
    }

    //调用CommandQueue类的execute()方法
    public void call() {
        commandQueue.execute();
    }
}
