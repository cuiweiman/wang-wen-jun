package com.wang.concurrent.template;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 实现一个 自定义 独占式 可重入锁（ReentrantLock）
 * 继承Lock接口，并实现一个继承 抽象同步器 的内部类
 */
public class SelfLock implements Lock {

    /**
     * 继承 AbstractQueuedSynchronizer 的内部类
     */
    private static class Sync extends AbstractQueuedSynchronizer {

        /**
         * 判断 当前锁 是否已占用：state=1 有线程获取到锁；state=0 没有线程拿到锁。
         *
         * @return
         */
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        /**
         * 尝试获取锁
         *
         * @param arg
         * @return
         */
        @Override
        protected boolean tryAcquire(int arg) {
            /**
             * compareAndSetState(expect,update);期望值0——当前没有线程拿到锁；更新至1——尝试获取锁。
             * 若结果为TRUE，获取锁成功，否则失败。
             */
            if (compareAndSetState(0, 1)) {
                // 表示当前线程 拿到了锁
                setExclusiveOwnerThread(Thread.currentThread());
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }

        /**
         * 尝试释放锁
         *
         * @param arg
         * @return
         */
        @Override
        protected boolean tryRelease(int arg) {
            // 判断 当前 是否有线程获取到锁。
            if (getState() == 0) {
                throw new UnsupportedOperationException();
            }
            // 存在线程获取到锁，将占有锁的线程置为null
            setExclusiveOwnerThread(null);
            // 释放锁 只有一个线程能释放，因此无需进行 CAS原子操作
            setState(0);
            return Boolean.TRUE;
        }

        Condition newCondition() {
            return new ConditionObject();
        }

    }

    private final Sync sync = new Sync();

    @Override
    public void lock() {
        sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}
