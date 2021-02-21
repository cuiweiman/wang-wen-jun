package com.wang.netty.bilibili.nio.buffer;

import java.nio.ByteBuffer;

/**
 * @description: 将 普通 Buffer 转换成 只读Buffer
 * @date: 2021/2/21 23:02
 * @author: wei·man cui
 */
public class ReadOnlyBuffer {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
        for (int i = 0; i < 64; i++) {
            byteBuffer.put((byte) i);
        }

        byteBuffer.flip();

        // 获取 只读的 Buffer
        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();
        System.out.println(readOnlyBuffer.getClass());
        while (readOnlyBuffer.hasRemaining()) {
            System.out.println(readOnlyBuffer.get());
        }

        readOnlyBuffer.put((byte)90);
    }
}
