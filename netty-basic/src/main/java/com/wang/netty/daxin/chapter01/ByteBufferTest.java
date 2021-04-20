package com.wang.netty.daxin.chapter01;

import java.nio.ByteBuffer;

/**
 * {@link java.nio.HeapByteBuffer}：堆内内存,内部使用 byte[] 存取数据；
 * {@link java.nio.DirectByteBuffer}：堆外内存，内部使用 Unsafe 存取数据；
 *
 * @description: ByteBuffer 的内存操作，管理堆内内存{@code HeapByteBuffer} 和 堆外内存/直接内存 {@code DirectByteBuffer}
 * @author: wei·man cui
 * @date: 2021/4/20 14:50
 */
public class ByteBufferTest {
    public static void main(String[] args) {
        // 申请创建 堆内内存 HeapByteBuffer
        final ByteBuffer allocate = ByteBuffer.allocate(16);
        allocate.putInt(8855666);
        allocate.put((byte) 127);
        allocate.flip();
        System.out.println(allocate.getInt());
        System.out.println(allocate.get());
        allocate.clear();

        final ByteBuffer allocateDirect = ByteBuffer.allocateDirect(16);
        allocateDirect.put((byte) 25);
        allocateDirect.flip();
        System.out.println(allocateDirect.get());

    }
}
