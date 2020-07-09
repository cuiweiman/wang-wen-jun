package com.cui.creator.singleton;

/**
 * 可以实现对象的延迟加载，并在保证线程安全的情况下使系统性能不受到影响。
 *
 * @description: Initialization on Demand Holder
 * @author: weiman cui
 * @date: 2020/7/9 17:16
 */
public class Singleton {
    private Singleton() {
    }

    /**
     * 静态单例对象 没有作为 Singleton 的成员变量而直接实例化
     * 因此类加载时不会实例化。在首次调用getInstance方法时，
     * 内部类HolderClass中定义了static类型的变量instance，此时会首先初始化这个成员变量，
     * 并且由Java虚拟机来保证其线程安全性，确保该成员变量只能初始化一次。
     * 由于你没有任何线程锁定，因此不会对性能造成任何影响。
     */
    private static class HolderClass {
        private final static Singleton instance = new Singleton();
    }

    public static Singleton getInstance() {
        return HolderClass.instance;
    }

    public static void main(String[] args) {
        Singleton s1, s2;
        s1 = Singleton.getInstance();
        s2 = Singleton.getInstance();
        System.out.println(s1 == s2);
    }
}
