package com.wang.netty.daxin.chapter03utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;

/**
 * ByteBuf 使用完毕后一定要记得释放，否则会造成内存泄漏。
 *
 * @description: ByteBuf API
 * @author: wei·man cui
 * @date: 2021/4/22 11:08
 */
public class ByteBufTest {

    /**
     * {@link ByteBuf#retain()} 增加引用计数；
     * {@link ByteBuf#release()} 减少引用计数，如果为 0 会释放底层资源。
     */
    public static void byteBufRetailAndRelease() {
        final ByteBuf pooledHeapBuffer = PooledByteBufAllocator.DEFAULT.heapBuffer(10);
        System.out.println(pooledHeapBuffer.refCnt());
        // ByteBuf 创建后 引用计数 为 1，又增加了 两次，减少了 一次。
        pooledHeapBuffer.retain();
        pooledHeapBuffer.retain();
        pooledHeapBuffer.release();
        System.out.println(pooledHeapBuffer.refCnt());
    }

    /**
     * 非池化/池化的的 ByteBuf 堆内和堆外内存的操作
     */
    public static void byteBufTest() {
        // 堆内 非池化 内存
        final ByteBuf unPooledHeapBuffer = UnpooledByteBufAllocator.DEFAULT.heapBuffer(8192);
        // 堆外 非池化 直接内存
        final ByteBuf unPooledDirectBuffer = UnpooledByteBufAllocator.DEFAULT.directBuffer(8192);

        // 所谓的池化，即内存中分配出一个 内存池，
        final ByteBuf pooledHeapBuffer = PooledByteBufAllocator.DEFAULT.heapBuffer(8192);
        final ByteBuf pooledHeapBuffer2 = PooledByteBufAllocator.DEFAULT.heapBuffer(8192);
        final ByteBuf pooledDirectBuffer = PooledByteBufAllocator.DEFAULT.directBuffer(8192);
        System.out.println("debug 查看对象信息：" + pooledHeapBuffer.readerIndex() + " " + pooledHeapBuffer.writerIndex());

        pooledHeapBuffer.writeByte(1);
        pooledHeapBuffer.writeByte(2);
        pooledHeapBuffer.writeByte(3);
        System.out.println("debug 查看对象信息：" + pooledHeapBuffer.readerIndex() + " " + pooledHeapBuffer.writerIndex());
        System.out.println(pooledHeapBuffer.readByte());
        System.out.println(pooledHeapBuffer.readByte());
        System.out.println("debug 查看对象信息：" + pooledHeapBuffer.readerIndex() + " " + pooledHeapBuffer.writerIndex());
        System.out.println("debug 查看对象信息，剩余可读/可写：" + pooledHeapBuffer.readableBytes() + " " + pooledHeapBuffer.writableBytes());
        System.out.println("引用个数：" + pooledHeapBuffer.refCnt());
        pooledHeapBuffer.release();
        System.out.println("引用个数：" + pooledHeapBuffer.refCnt());
        pooledHeapBuffer2.release();
    }

    public static void main(String[] args) {
        // byteBufTest();
        byteBufRetailAndRelease();
    }
}
