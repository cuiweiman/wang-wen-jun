package com.wang.concurrent.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 显示锁 Lock 的核心方法
 * 使用显示锁，必须在finally关键字中释放锁
 */
public class LockDemo {

    private Lock lock = new ReentrantLock();
    private int count;

    /**
     * 累加
     */
    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }

    public synchronized void increment2() {
        count++;
    }


}
/**
 * finally代码块不被执行的情况：
 * 1. System.exit(); 中止当前虚拟机，虚拟机被中止了，finally代码块不会被执行。
 * 2. 守护线程中的 finally。当非守护线程执行结束后，该线程的守护线程也会强制退出。
 */