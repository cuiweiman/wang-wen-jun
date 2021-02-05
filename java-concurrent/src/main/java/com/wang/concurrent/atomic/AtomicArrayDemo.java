package com.wang.concurrent.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class AtomicArrayDemo {

    static int[] value = new int[]{1, 2};

    static AtomicIntegerArray ai = new AtomicIntegerArray(value);

    public static void main(String[] args) {
        /**
         * getAndSet方法 修改了 AtomicIntegerArray 数组对象中的 元素内容
         * 但 对原数组中的值 是 没有任何影响的。
         */
        int old = ai.getAndSet(0, 3);
        System.out.println("oldValue=" + old);
        System.out.println("newValue=" + ai.get(0));
        System.out.println("array's Value=" + value[0]);
    }

}
