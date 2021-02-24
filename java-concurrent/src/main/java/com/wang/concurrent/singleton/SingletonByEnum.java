package com.wang.concurrent.singleton;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 创建一个 private 构造函数的类，实现其静态方法。然后在其内部创建一个 enum 枚举，来实现该类的实例创建。
 *
 * @description: 枚举方式 实现懒加载：枚举实现的单例是天生线程安全的。
 * 枚举，在第一次被真正用到的时候，会被虚拟机加载并初始化，而这个初始化过程是线程安全的。
 * @date: 2021/2/23 23:11
 * @author: wei·man cui
 */
public class SingletonByEnum {

    private final ConcurrentHashMap<String, Object> cacheMap;

    private SingletonByEnum() {
        cacheMap = new ConcurrentHashMap<>();
    }

    public static SingletonByEnum getInstance() {
        return Singleton.INSTANCE.getInstance();
    }

    private enum Singleton {
        /**
         * 枚举单例对象，获取本类实例
         */
        INSTANCE;

        private final SingletonByEnum instance;

        Singleton() {
            instance = new SingletonByEnum();
        }

        private SingletonByEnum getInstance() {
            return instance;
        }
    }


    public static void main(String[] args) {
        SingletonByEnum instance = SingletonByEnum.getInstance();
        instance.cacheMap.put("a", "1");
        System.out.println(instance.cacheMap.get("a"));
    }

}
