package com.cui.creator.singleton;

/**
 * @description: 使用枚举 构造单例模式
 * @author: wei·man cui
 * @date: 2020/7/10 9:14
 */
public class EnumSingleton {

    /**
     * 枚举类 只会被加载一次，因此是线程安全的
     */
    private enum Singleton {
        /**
         * 单例
         */
        INSTANCE;
        private final EnumSingleton instance;

        Singleton() {
            instance = new EnumSingleton();
        }

        private EnumSingleton getInstance() {
            return instance;
        }
    }

    public static EnumSingleton getInstance() {
        return Singleton.INSTANCE.getInstance();
    }

    public static void main(String[] args) {
        EnumSingleton e1 = EnumSingleton.getInstance();
        EnumSingleton e2 = EnumSingleton.getInstance();
        System.out.println(e1 == e2);
    }

}
