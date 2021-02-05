package com.wang.concurrent.template;

import java.time.LocalDateTime;

/**
 * 发送 消息 的一个基类 。 展示 模板模式 的使用。
 * 流程方法——需要子类实现，模板方法，子类不需要实现，直接调用即可。
 */
public abstract class SendCustomer {

    // 流程方法——接收者
    public abstract void to();

    // 流程方法——发送者
    public abstract void from();

    // 流程方法——发送内容
    public abstract void content();

    // 发送日期
    public void date() {
        System.out.println(LocalDateTime.now());
    }

    // 流程方法——发送
    public abstract void send();

    /**
     * 模板方法
     */
    public void sendMsg() {
        to();
        from();
        content();
        date();
        send();
    }

}


/**
 * 补充：抽象类与接口的区别
 * 1. 接口的抽象级别最高，不能有构造函数，也不能有方法实现。
 * 2. 接口里的变量必须是公共静态常量，抽象类中的变量可以是普通变量。
 */