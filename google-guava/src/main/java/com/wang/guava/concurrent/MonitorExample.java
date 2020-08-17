package com.wang.guava.concurrent;

import com.google.common.util.concurrent.Monitor;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.currentThread;

/**
 * @description: Monitor 分别使用 Synchronized、Lock、Monitor 实现生产者/消费者模式
 * @author: wei·man cui
 * @date: 2020/8/17 11:43
 */
public class MonitorExample {

    public static void main(String[] args) {
        // Synchronized 实现生产者与消费者
        // final Synchroinzed service = new Synchroinzed();

        // Lock 实现生产者与消费者
        // final LockCondition service = new LockCondition();

        // Monitor 实现生产者与消费者
        final MonitorGuard service = new MonitorGuard();

        final AtomicInteger COUNTER = new AtomicInteger(0);

        /**
         * 生产
         */
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                for (; ; ) {
                    try {
                        int data = COUNTER.getAndIncrement();
                        System.out.println(currentThread() + " offer " + data);
                        service.offer(data);
                        TimeUnit.MILLISECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        /**
         * 消费
         */
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                for (; ; ) {
                    try {
                        int data = service.take();
                        System.out.println(currentThread() + " take " + data);
                        TimeUnit.MILLISECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    /**
     * Monitor 实现生产者与消费者
     */
    static class MonitorGuard {

        private final LinkedList<Integer> queue = new LinkedList<>();

        private final int MAX = 10;

        private final Monitor monitor = new Monitor();

        private final Monitor.Guard CAN_OFFER = monitor.newGuard(() -> queue.size() < MAX);

        private final Monitor.Guard CAN_TAKE = monitor.newGuard(() -> !queue.isEmpty());

        public void offer(int value) {
            try {
                monitor.enterWhen(CAN_OFFER);
                queue.addLast(value);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                monitor.leave();
            }
        }

        public int take() {
            try {
                monitor.enterWhen(CAN_TAKE);
                return queue.removeLast();
            } catch (InterruptedException e) {
                throw new RuntimeException();
            } finally {
                monitor.leave();
            }
        }

    }

    /**
     * Lock 实现生产者与消费者
     */
    static class LockCondition {
        private final ReentrantLock lock = new ReentrantLock();

        private final Condition FULL_CONDITION = lock.newCondition();

        private final Condition EMPTY_CONDITION = lock.newCondition();

        private final LinkedList<Integer> queue = new LinkedList<>();

        private final int MAX = 10;

        public void offer(int value) {
            try {
                lock.lock();
                while (queue.size() >= MAX) {
                    FULL_CONDITION.await();
                }
                queue.addLast(value);
                // 放入队列后，通知 消费者
                EMPTY_CONDITION.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public int take() {
            Integer value = null;
            try {
                lock.lock();
                while (queue.isEmpty()) {
                    EMPTY_CONDITION.await();
                }
                value = queue.removeFirst();
                // 取出值后，通知 生产者
                FULL_CONDITION.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            return value;
        }
    }

    /**
     * Synchronized 实现生产者与消费者
     */
    static class Synchroinzed {
        private final LinkedList<Integer> queue = new LinkedList<>();

        private final int MAX = 10;

        // 放入 容器
        public void offer(int value) {
            synchronized (queue) {
                while (queue.size() >= MAX) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                queue.addLast(value);
                queue.notifyAll();
            }
        }

        // 容器中 取出
        public int take() {
            synchronized (queue) {
                while (queue.isEmpty()) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Integer val = queue.removeFirst();
                queue.notifyAll();
                return val;
            }
        }
    }
}
