package com.wang.concurrent.singleton;

/**
 * 懒汉式——双重检查.由于代码过于复杂，双重检查现在已经不实用了。
 *
 * @author weiman cui
 * @date 2020/5/18 22:02
 */
public class SingletonDcl {
    private static volatile SingletonDcl singletonDcl;

    private SingletonDcl() {
    }

    /**
     * 懒汉式——双重检测机制，获取单例模式。
     * 双重检查：防止在 首次判定、获取锁的过程中，其它线程完成了实例化。
     * 但并不是完全的线程安全：因为构造函数不是一个原子操作；new实例时，存在可能其他线程new出了
     * 对象的引用，本线程判断引用不为null，直接返回了，但对象的属性不一定完成赋值。
     * 解决方法：变量上使用volatile关键字。
     *
     * @return
     */
    public static SingletonDcl getInstance() {
        if (singletonDcl == null) {
            synchronized (SingletonDcl.class) {
                if (singletonDcl == null) {
                    singletonDcl = new SingletonDcl();
                }
            }
        }
        return singletonDcl;
    }


}
