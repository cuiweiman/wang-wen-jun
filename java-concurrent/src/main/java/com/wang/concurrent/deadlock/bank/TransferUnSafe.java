package com.wang.concurrent.deadlock.bank;

/**
 * 银行转账操作实现类——非线程安全
 * 先锁 转出对象，再锁 转入对象
 * 当转出对象转账给转入对象，同时转入->转出对象是，会发生动态顺序死锁。
 * 虽然方法中固定了获取锁的顺序，但是调用者传参不同，无法保证获取锁的顺序
 *
 * @author weiman cui
 * @date 2020/5/17 18:12
 */
public class TransferUnSafe implements Transfer {
    @Override
    public void transferAccount(UserAccount from,
                                UserAccount to,
                                int amount) throws InterruptedException {
        synchronized (from) {
            System.out.println(Thread.currentThread().getName() + " 获取到锁： " + from.getName());
            Thread.sleep(100);
            synchronized (to) {
                System.out.println(Thread.currentThread().getName() + " 获取到锁： " + to.getName());
                from.flyMoney(amount);
                to.addMoney(amount);
            }
        }


    }
}
