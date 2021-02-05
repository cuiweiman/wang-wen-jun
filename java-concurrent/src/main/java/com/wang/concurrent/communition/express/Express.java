package com.wang.concurrent.communition.express;

/**
 * 为满足条件，就进入等待。
 * 满足条件后，被唤醒。
 * wait/notify/notifyAll
 */
public class Express {

    public final static String CITY = "ShangHai";

    private int km;

    private String site;

    public Express(int km, String site) {
        this.km = km;
        this.site = site;
    }

    /**
     * 公里数 变化，通知处于 wait状态 并 需要 处理公里数 的线程进行业务处理
     */
    public synchronized void changeKm() {
        this.km = 101;
        notify();
    }


    /**
     * 地点 变化，通知处于 wait状态 并 需要 处理地点 的线程进行业务处理
     */
    public synchronized void changeSite() {
        this.site = "BeiJing";
        notify();
    }

    public synchronized void waitKm() {
        try {
            while (this.km <= 100) {
                wait();
                System.out.println("【KM】Thread.ID：" + Thread.currentThread().getId() + " be notified");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("The KM is " + this.km);
    }

    public synchronized void waitSite() {
        try {
            while (CITY.equals(this.site)) {
                wait();
                System.out.println("【Site】Thread.ID：" + Thread.currentThread().getId() + " be notified");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("The Site is " + this.site);
    }
}
