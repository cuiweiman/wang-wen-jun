package com.wang.netty.daxin.chapter09nettypool;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

/**
 * Netty 分配内存时，若内存块>8192，那么是 PoolChunk 分配的；
 *
 * @description: PoolChunk 测试
 * @author: wei·man cui
 * @date: 2021/4/28 17:22
 */
public class PoolChunkTest1 {
    public static void main(String[] args) {
        final PooledByteBufAllocator allocator = PooledByteBufAllocator.DEFAULT;
        // 默认使用 堆外内存
        allocator.buffer(1024);
        // 明确指定 堆内内存
        final ByteBuf byteBuf = allocator.heapBuffer(8192);
        // 明确指定 堆外内存
        allocator.directBuffer(1024);

    }
}
