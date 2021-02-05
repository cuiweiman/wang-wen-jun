package com.wang.concurrent.safeend;

/**
 * 安全的中断线程——发生InterruptedException异常
 */
public class HasInterruptedException {

    private static class UseThread extends Thread {
        public UseThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            // 判断 线程是否 中断，并返回当前 线程的 中断标志位
            while (!isInterrupted()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // 抛出 InterruptedException 异常，线程中断标志位会被复原为FALSE。可以在处理该异常时，重新做中断操作。
                    System.out.println(threadName + "InterruptedException interrupt flag is " + isInterrupted());
                    Thread.currentThread().interrupt();
                    System.out.println(threadName + "InterruptedException REPEAT  interrupt flag is " + isInterrupted());
                    e.printStackTrace();
                }
                System.out.println(threadName + "After TRY/CATCH interrupt flag is " + isInterrupted());
            }
            System.out.println(threadName + "After While interrupt flag is " + isInterrupted());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new UseThread("[HasInterruptedException]");
        thread.start();
        Thread.sleep(500);
        // 中断线程，将中断标志位 置为 TRUE
        thread.interrupt();
    }

}
