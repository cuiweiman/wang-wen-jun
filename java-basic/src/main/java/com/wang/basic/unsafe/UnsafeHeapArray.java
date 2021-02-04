package com.wang.basic.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * JVM内存不足时，会频繁GC。理想情况下可以考虑使用 堆外内存（不受JVM管理，因此也需要手动释放）。
 * 使用 {@link Unsafe#allocateMemory(long)}分配堆外内存，并进行管理
 *
 * @description: sum.misc.Unsafe类 在堆外创建一个巨大的 int 数组。
 * @author: wei·man cui
 * @date: 2021/2/4 13:32
 */
public class UnsafeHeapArray {

    /**
     * 测试方法
     *
     * @param args java命令行参数
     */
    public static void main(String[] args) {
        UnsafeHeapArray heapArray = new UnsafeHeapArray(4);
        heapArray.set(0, 1);
        heapArray.set(1, 2);
        heapArray.set(2, 3);
        heapArray.set(3, 4);
        // 索引2 处重复放入元素
        heapArray.set(2, 5);
        int sum = 0;
        for (int i = 0; i < heapArray.size; i++) {
            sum += heapArray.get(i);
        }
        System.out.println(sum);
        heapArray.freeMemory();
    }


    private static final int INT = 4;

    private static Unsafe unsafe;
    private long size;
    private long address;

    static {
        try {
            // 获取 Unsafe 类实例，只能通过 反射机制 获取
            final Field field = Unsafe.class.getDeclaredField("theUnsafe");
            // Unsafe.theUnsafe 属性是 private 修饰的，需要修改获取权限才能拿到。
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public UnsafeHeapArray(long size) {
        this.size = size;
        this.address = unsafe.allocateMemory(size * INT);
    }

    /**
     * 获取指定索引处的元素
     *
     * @param i 指定索引
     * @return 指定索引处的元素
     */
    public int get(int i) {
        return unsafe.getInt(address + i * INT);
    }

    /**
     * 设置 指定索引处  的元素
     *
     * @param i   指定索引
     * @param val 元素值
     */
    public void set(long i, int val) {
        unsafe.putInt(address + i * INT, val);
    }

    public long size() {
        // 元素个数
        return size;
    }

    /**
     * 释放堆外内存
     */
    public void freeMemory() {
        unsafe.freeMemory(address);
    }

}
