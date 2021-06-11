package com.wang.concurrent.locks;


import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 分别使用 Synchronized 和 ReentrantLock 实现可重入锁
 */
public class ReentrantLockDemo {

    @Test
    public void synchronizedTest() {
        new Thread(() -> {
            synchronized (this) {
                System.out.println("第1次获得锁，这个锁是：" + this);
                int index = 1;
                while (true) {
                    synchronized (this) {
                        System.out.println("第" + (++index) + "次获得锁，这个锁是：" + this);
                    }
                    if (index == 10) {
                        break;
                    }
                }
            }
        }).start();
    }


    @Test
    public void reentrantLockTest() {
        ReentrantLock lock = new ReentrantLock();
        new Thread(() -> {
            try {
                lock.lock();
                System.out.println("第1次获得锁，这个锁是：" + lock);
                int index = 1;
                while (true) {
                    try {
                        lock.lock();
                        System.out.println("第" + (++index) + "次获得锁，这个锁是：" + lock);
                        if (index == 10) {
                            break;
                        }
                    } finally {
                        lock.unlock();
                    }
                }
            } finally {
                lock.unlock();
            }
        }).start();
    }
}
