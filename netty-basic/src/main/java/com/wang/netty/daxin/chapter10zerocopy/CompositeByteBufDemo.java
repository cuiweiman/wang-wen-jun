package com.wang.netty.daxin.chapter10zerocopy;

import io.netty.buffer.*;

/**
 * @description: 零拷贝方式  合并 多个 ByteBuf
 * @author: wei·man cui
 * @date: 2021/4/29 9:46
 */
public class CompositeByteBufDemo {

    public static void compositeByteBufTest() {
        ByteBuf directBuf = UnpooledByteBufAllocator.DEFAULT.buffer();
        ByteBuf heapBuf = UnpooledByteBufAllocator.DEFAULT.heapBuffer();
        directBuf.writeByte(1);
        heapBuf.writeByte(2);

        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();
        compositeByteBuf.addComponent(true, directBuf);
        compositeByteBuf.addComponent(true, heapBuf);
        System.out.println(compositeByteBuf.readByte());
        System.out.println(compositeByteBuf.readByte());
        compositeByteBuf.release();

        System.out.println(directBuf.refCnt());
        System.out.println(heapBuf.refCnt());
    }

    public static void tradition() {
        final ByteBuf directBuf = UnpooledByteBufAllocator.DEFAULT.buffer();
        final ByteBuf heapBuf = UnpooledByteBufAllocator.DEFAULT.heapBuffer();
        directBuf.writeByte(1);
        heapBuf.writeByte(2);

        final ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(1024);
        byteBuf.writeBytes(directBuf);
        byteBuf.writeBytes(heapBuf);
        System.out.println(byteBuf.readByte());
        System.out.println(byteBuf.readByte());

        directBuf.release();
        heapBuf.release();

    }


    public static void main(String[] args) {
        // tradition();
        compositeByteBufTest();
    }
}
