package com.wang.concurrent.deadlock.bank;

/**
 * 银行转账操作实现类——线程安全,保证获取锁顺序的一致性
 * 获取对象的HashCode，先锁定HashCode值小的，再锁定HashCode值大的锁对象。
 * 需注意发生hash碰撞问题。
 * （如果用户的账户ID是唯一的，也可以通过比较用户ID来保证锁顺序）
 *
 * @author weiman cui
 * @date 2020/5/17 20:30
 */
public class TransferSafe implements Transfer {
    /**
     * 发生hash冲突时，需要多获取一个锁，以保证锁的顺序
     */
    private static Object tieLock = new Object();

    @Override
    public void transferAccount(UserAccount from, UserAccount to, int amount) throws InterruptedException {

        // System.identityHashCode(Object o) 无论是否重写了HashCode方法，
        // 获取对象最原始的hashCode
        int formHash = System.identityHashCode(from);
        int toHash = System.identityHashCode(to);
        if (formHash < toHash) {
            synchronized (from) {
                System.out.println(Thread.currentThread().getName() + " 获取到锁： " + from.getName());
                Thread.sleep(100);
                synchronized (to) {
                    System.out.println(Thread.currentThread().getName() + " 获取到锁： " + to.getName());
                    from.flyMoney(amount);
                    to.addMoney(amount);
                    System.out.println(Thread.currentThread().getName() + " " + from.getMoney() + " " + to.getMoney());
                }
            }
        } else if (formHash > toHash) {
            synchronized (to) {
                System.out.println(Thread.currentThread().getName() + " 获取到锁： " + to.getName());
                Thread.sleep(100);
                synchronized (from) {
                    System.out.println(Thread.currentThread().getName() + " 获取到锁： " + from.getName());
                    from.flyMoney(amount);
                    to.addMoney(amount);
                    System.out.println(Thread.currentThread().getName() + " " + from.getMoney() + " " + to.getMoney());
                }
            }
        } else {
            synchronized (tieLock) {
                System.out.println(Thread.currentThread().getName() + " Hash冲突，获取到tieLock锁");
                synchronized (from) {
                    System.out.println(Thread.currentThread().getName() + " 获取到锁： " + from.getName());
                    Thread.sleep(100);
                    synchronized (to) {
                        System.out.println(Thread.currentThread().getName() + " 获取到锁： " + to.getName());
                        from.flyMoney(amount);
                        to.addMoney(amount);
                        System.out.println(Thread.currentThread().getName() + " " + from.getMoney() + " " + to.getMoney());
                    }
                }
            }
        }
    }
}
