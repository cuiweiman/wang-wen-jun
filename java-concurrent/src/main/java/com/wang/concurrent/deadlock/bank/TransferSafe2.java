package com.wang.concurrent.deadlock.bank;

import java.util.Random;

/**
 * 银行转账操作实现类——线程安全：尝试获取锁
 * 需要给UserAccount开启一个 显示锁 Lock lock = new ReentrantLock();
 * 未获取到锁时，为什么要休眠？
 *  Jack2Rose线程拿到了Jack锁，将要去获取Rose锁，此时rose2Jack线程拿到了Rose锁，将要获取Jack锁
 *  两个线程都没能成功获取到锁，都会主动释放锁，再次去获取。若又发生此情况，会死循环，即所谓的“活锁”。
 *  因此使用 休眠，两个线程错开获取锁的时间段，避免同时去获取锁发生以上情形。
 *
 * @author weiman cui
 * @date 2020/5/17 22:11
 */
public class TransferSafe2 implements Transfer {
    @Override
    public void transferAccount(UserAccount from, UserAccount to, int amount) throws InterruptedException {
        Random r = new Random();
        while (true) {
            if (from.getLock().tryLock()) {
                try {
                    if (to.getLock().tryLock()) {
                        try {
                            from.flyMoney(amount);
                            to.addMoney(amount);
                            System.out.println(Thread.currentThread().getName() + " " + from.getMoney() + " " + to.getMoney());
                            break;
                        } finally {
                            to.getLock().unlock();
                        }
                    }
                } finally {
                    from.getLock().unlock();
                }
            }
        }
        // 未获取到锁，休眠10ms以内
        Thread.sleep(r.nextInt(10));
    }
}
