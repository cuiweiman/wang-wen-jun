package com.wang.concurrent.demo1;

public class StartAndRun {

    public static class ThreadRun extends Thread {
        @Override
        public void run() {
            int i = 90;
            while (i > 0) {
                SleepTools.ms(1000);
                System.out.println(Thread.currentThread().getName() + " called and now the i = " + i--);
            }
        }
    }

    public static void main(String[] args) {
        ThreadRun beCalled = new ThreadRun();
        beCalled.setName("BeCalled");
        // 线程调用
        beCalled.start();

        // 普通方法调用
        beCalled.run();
    }

}
