package com.cui.creator.singleton;

/**
 * @description: 懒汉式 延迟加载
 * @author: wei·man cui
 * @date: 2020/7/9 16:30
 */
public class LazySingleton {

    // private static LazySingleton instance = null;

    // 双重检查锁定
    private volatile static LazySingleton instance = null;

    private LazySingleton() {
    }

    // 双重检查锁定
    public static LazySingleton getInstance() {
        // 第一重 判断
        if (instance == null) {
            // 锁定代码块
            synchronized (LazySingleton.class) {
                // 第二重 判断
                if (instance == null) {
                    instance = new LazySingleton();
                }
            }
        }
        return instance;
    }


    // 影响 系统性能
    /*public synchronized static LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }*/

}
