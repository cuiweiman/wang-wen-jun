package com.wang.concurrent.singleton;

/**
 * 懒汉式——类初始化模式 线程安全的实现方法
 */
public class SingletonInit {

    private SingletonInit() {
    }

    private static class InstanceHolder {
        public static SingletonInit instance = new SingletonInit();
    }

    public static SingletonInit getInstance() {
        return InstanceHolder.instance;
    }


}
