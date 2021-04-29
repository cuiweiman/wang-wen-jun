package com.wang.netty.daxin.chapter10zerocopy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;

/**
 * @description: 通过 wrap 操作，将 byte[] 数组、ByteBuf、ByteBuffer 等包装秤一个 Netty ByteBuf 对象，进而避免内存拷贝操作
 * @author: wei·man cui
 * @date: 2021/4/29 10:23
 */
public class UnpooledDemo {


    public static void main(String[] args) {
        final ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        // 会发生 内存拷贝
        final ByteBuf byteBuf1 = ByteBufAllocator.DEFAULT.buffer(byteBuffer.capacity()).writeBytes(byteBuffer);

        // 内存 零拷贝
        ByteBuf byteBuf2 = Unpooled.wrappedBuffer(byteBuffer);

        byteBuf1.release();
        byteBuf2.release();
    }

}
