package com.wang.concurrent.communition.express;

import com.wang.concurrent.demo1.SleepTools;

/**
 * Express类的测试
 */
public class TestExpress {
    // Object提供的 wait/notify/notifyAll
    // private static Express express = new Express(0, Express.CITY);

    // Condition提供的 await/signal/signalAll
    private static ExpressCondition express = new ExpressCondition(0, ExpressCondition.CITY);

    // 检查里程数变化的线程，不满足条件，一直等待
    private static class CheckKm extends Thread {
        @Override
        public void run() {
            express.waitKm();
        }
    }

    // 检查里程数变化的线程，不满足条件，一直等待
    private static class CheckSite extends Thread {
        @Override
        public void run() {
            express.waitSite();
        }
    }

    public static void main(String[] args) {

        for (int i = 0; i < 3; i++) {
            new CheckKm().start();
        }
        for (int i = 0; i < 3; i++) {
            new CheckSite().start();
        }
        SleepTools.ms(1000);
        // express.changeKm();
        express.changeSite();
    }

}
