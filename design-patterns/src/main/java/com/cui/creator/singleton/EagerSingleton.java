package com.cui.creator.singleton;

/**
 * @description: 饿汉式 单例类
 * @author: wei·man cui
 * @date: 2020/7/9 16:28
 */
public class EagerSingleton {
    private static final EagerSingleton instance = new EagerSingleton();

    /**
     * 需要将 构造方法私有化，不允许外部调用构造方法创建本类
     */
    private EagerSingleton() {
    }

    public static EagerSingleton getInstance() {
        return instance;
    }
}
