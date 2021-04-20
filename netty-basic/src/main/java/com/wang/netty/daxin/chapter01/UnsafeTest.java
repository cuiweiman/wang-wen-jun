package com.wang.netty.daxin.chapter01;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @description: unsafe 类的使用：创建堆外内存
 * @author: wei·man cui
 * @date: 2021/4/20 14:40
 */
public class UnsafeTest {

    public static void main(String[] args) {
        Unsafe unsafe = getUnsafe();

        // 分配 10 字节的内存，返回值为 内存寄出地址
        long address = unsafe.allocateMemory(10);

        // 传入 基础地址，长度为10，byte-0，初始化堆外内存
        unsafe.setMemory(address, 10L, (byte) 0);

        // 传入内存地址，设置 byte 值
        unsafe.putByte(address, (byte) 1);
        unsafe.putByte(address + 1, (byte) 2);
        unsafe.putByte(address + 2, (byte) 3);

        // 根据内存值，获取 byte值
        System.out.println("申请到的内存地址：address=" + address);
        System.out.println(unsafe.getByte(address));
        System.out.println(unsafe.getByte(address + 1));
        System.out.println(unsafe.getByte(address + 2));

        // 释放 堆外 内存
        unsafe.freeMemory(address);
    }


    /**
     * 反射获取到 Unsafe 类对象
     *
     * @return Unsafe
     */
    private static Unsafe getUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


}
