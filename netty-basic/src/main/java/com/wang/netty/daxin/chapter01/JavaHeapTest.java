package com.wang.netty.daxin.chapter01;

/**
 * @description: Java 堆内存和栈内存
 * @author: wei·man cui
 * @date: 2021/4/20 14:35
 */
public class JavaHeapTest {

    public static void main(String[] args) {
        // content 变量 存放在 栈内存中，地址指向堆内存中的对象所在位置
        // new byte[] 对象 存放在 堆内存中，eden 区
        byte[] content = new byte[1024];

    }

}
