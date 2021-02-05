package com.wang.concurrent.homework;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 模拟CAS的循环操作,实现线程安全的自增操作
 * 在进行CAS操作时，若不成功则一直循环，直到成功为止
 */
public class HalfAtomicInt {

    private AtomicInteger ai = new AtomicInteger(0);

    @Test
    public void testCAS() {
        increment();
        System.out.println(getCount());
    }

    public void increment() {
        for (; ; ) {
            int i = ai.get();
            boolean suc = ai.compareAndSet(i, ++i);
            if (suc) {
                break;
            }
        }
    }

    public int getCount() {
        return ai.get();
    }

}
