package com.wang.concurrent.communition.express;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 为满足条件，就进入等待。
 * 满足条件后，被唤醒。
 * await/signal/signalAll
 */
public class ExpressCondition {

    public final static String CITY = "ShangHai";
    private int km;
    private String site;

    private Lock kmLock = new ReentrantLock();
    private Condition kmCond = kmLock.newCondition();

    private Lock siteLock = new ReentrantLock();
    private Condition siteCond = siteLock.newCondition();

    public ExpressCondition(int km, String site) {
        this.km = km;
        this.site = site;
    }

    /**
     * 公里数 变化，通知处于 wait状态 并 需要 处理公里数 的线程进行业务处理
     */
    public void changeKm() {
        kmLock.lock();
        try {
            this.km = 101;
            kmCond.signal();
        } finally {
            kmLock.unlock();
        }
    }


    /**
     * 地点 变化，通知处于 wait状态 并 需要 处理地点 的线程进行业务处理
     */
    public void changeSite() {
        siteLock.lock();
        try {
            this.site = "BeiJing";
            siteCond.signal();
        } finally {
            siteLock.unlock();
        }
    }

    public void waitKm() {
        kmLock.lock();
        try {
            while (this.km <= 100) {
                kmCond.await();
                System.out.println("【KM】Thread.ID：" + Thread.currentThread().getId() + " be notified");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            kmLock.unlock();
        }
        System.out.println("The KM is " + this.km);
    }

    public void waitSite() {
        siteLock.lock();
        try {
            while (CITY.equals(this.site)) {
                siteCond.await();
                System.out.println("【Site】Thread.ID：" + Thread.currentThread().getId() + " be notified");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            siteLock.unlock();
        }
        System.out.println("The Site is " + this.site);
    }
}
