package com.wang.concurrent.singleton;

/**
 * 单例模式——饿汉式：加载类时，就创建出类实例
 * 多个线程对同一个类进行加载，虚拟机内部会对该类进行加锁，保证了线程安全
 *
 * @author weiman cui
 * @date 2020/5/18 22:12
 */
public class SingletonEhan {
    public static SingletonEhan singletonEHan = new SingletonEhan();

    private SingletonEhan() {
    }
}
