package com.wang.concurrent.atomic;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 带版本戳的原子操作类
 */
public class AtomicStampedReferenceDemo {

    /**
     * initialRef：初始化 值
     * initialStamp：初始化 版本戳
     */
    static AtomicStampedReference<String> asr = new AtomicStampedReference<>("Jack", 0);

    public static void main(String[] args) throws Exception {
        // 获取初始的 版本号 he 原值
        final int oldStamp = asr.getStamp();
        final String oldReference = asr.getReference();

        System.out.println(oldStamp + "=====" + oldReference);

        Thread rightStampThread = new Thread(() -> System.out.println(Thread.currentThread().getName()
                + " 当前变量值：" + oldReference + " ； 当前本版号：" + oldStamp + "——"
                + asr.compareAndSet(oldReference, oldReference + " Java", oldStamp, oldStamp + 1)));

        // 使用错误的 版本戳，演示运行效果
        Thread errorStampThread = new Thread(() -> System.out.println(Thread.currentThread().getName()
                + " 当前变量值：" + asr.getReference() + " ； 当前本版号：" + asr.getStamp() + "——"
                + asr.compareAndSet(asr.getReference(), asr.getReference() + " C", asr.getStamp(), oldStamp + 1)));

        rightStampThread.start();
        // join方法：阻塞，直到rightStampThread线程执行结束
        rightStampThread.join();

        errorStampThread.start();
        // join方法：阻塞，直到errorStampThread线程执行结束
        errorStampThread.join();
        System.out.println(asr.getStamp() + "=====" + asr.getReference());
    }

}
