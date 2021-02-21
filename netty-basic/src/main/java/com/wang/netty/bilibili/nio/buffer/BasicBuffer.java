package com.wang.netty.bilibili.nio.buffer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * @description: NIO 中 Buffer 的基础使用
 * @date: 2021/2/21 22:57
 * @author: wei·man cui
 */
public class BasicBuffer {
    public static void main(String[] args) {
        // 创建一个 可以存放5个int 数据的 Buffer
        IntBuffer intBuffer = IntBuffer.allocate(10);
        // 写数据到Buffer
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i);
        }
        // Buffer 的 读写切换——读
        intBuffer.flip();
        while (intBuffer.hasRemaining()) {
            // Buffer.get()是指针的后移
            System.out.println(intBuffer.get());
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.putChar('a');
        byteBuffer.flip();
        System.out.println(byteBuffer.getChar());
    }

}
