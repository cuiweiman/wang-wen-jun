package com.wang.netty.daxin.chapter03utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

/**
 * {@link #duplicateAndRetailedDuplicate} 复制缓冲区的方法
 * {@link #sliceAndRetainedSlice}
 *
 * @description: ByteBuf的一些高级？ API
 * @author: wei·man cui
 * @date: 2021/4/22 14:54
 */
public class ByteBufAdvance {

    public static void main(String[] args) {
        // duplicateAndRetailedDuplicate();
        // sliceAndRetainedSlice();
        readSliceAndReadRetainedSlice();
    }

    /**
     * 获取部分空间，彼此共享底层缓冲区，会增加原缓冲区的 readerIndex
     * {@link ByteBuf#readSlice(int)}
     * {@link ByteBuf#readRetainedSlice(int)}
     */
    public static void readSliceAndReadRetainedSlice() {
        final ByteBuf buf = PooledByteBufAllocator.DEFAULT.heapBuffer(10);
        buf.writeByte(11);
        buf.writeByte(22);
        buf.writeByte(33);
        System.out.println("原坐标的读写标识：" + buf.readerIndex() + "|" + buf.writerIndex());
        final ByteBuf readSlice = buf.readSlice(2);
        System.out.println("新坐标的读写标识：" + readSlice.readerIndex() + "|" + readSlice.writerIndex());


    }

    /**
     * {@link ByteBuf#slice(int, int)} 从当前缓冲区中 截取出的缓冲区（类似于视图），
     * 依然共用缓冲区，修改缓冲区数据时会互相影响。
     * {@link ByteBuf#retainedSlice(int, int)} 会 使 引用计数增一。
     */
    public static void sliceAndRetainedSlice() {
        final ByteBuf buf = PooledByteBufAllocator.DEFAULT.heapBuffer(10);
        buf.writeByte(11);
        buf.writeByte(22);
        buf.writeByte(33);
        System.out.println(buf.readByte() + " " + buf.readByte());
        System.out.println("原坐标的读写标识：" + buf.readerIndex() + "|" + buf.writerIndex());
        final ByteBuf slice = buf.slice(buf.readerIndex(), buf.readableBytes());
        System.out.println("新坐标的读写标识：" + slice.readerIndex() + "|" + slice.writerIndex());
        System.out.println(slice.readByte());
        // slice 的 writerIndex=3，在写入的话，会从 4 开始写，超出了 界限，会抛出越界异常。
        // slice.writeByte(66);
        // slice 的 readerIndex=0，writerIndex=1，即不能再读了，也不能再写了，只有 0 这一个位置。
        // 修改后，也会影响到 原变量的值
        slice.setByte(0, 9);
        System.out.println(buf.readByte());
    }


    /**
     * 复制方法：返回一个 共享该缓冲区整个区域的缓冲区，如果修改返回的缓冲区 或 修改当前缓冲区的数据 会相互影响，
     * 同时它们维护单独的索引和标记。此方法不会修改此缓冲区的 readerIndex、writerIndex。
     * <p>
     * duplicate方法 会将当前缓冲区复制到一个新的变量中，新变量的 writerIndex和readerIndex与旧的变量都一样，然而他们指向的缓冲区也一样，因此
     * 在操作 新变量 或 旧变量 的缓冲区数据时，会发生影响（复制出来的变量和被复制的变量共用一个缓冲区）
     *
     * <p>
     * {@link ByteBuf#duplicate()}：从当前的 ByteBuf 对象中 复制出 ByteBuf 对象。不会增加缓冲区的引用计数器。
     * {@link ByteBuf#retainedDuplicate()} 函数功能与 duplicate 一样。会增加引用计数器。
     */
    public static void duplicateAndRetailedDuplicate() {
        final ByteBuf buf = PooledByteBufAllocator.DEFAULT.heapBuffer(10);
        buf.writeByte(11);
        buf.writeByte(22);
        buf.writeByte(33);
        System.out.println(buf.readerIndex() + "|" + buf.writerIndex());
        System.out.println(buf.readByte() + " " + buf.readByte() + " " + buf.readByte());
        System.out.println(buf.readerIndex() + "|" + buf.writerIndex());
        // 复制一个新的 ByteBuf
        final ByteBuf duplicate = buf.duplicate();
        System.out.println(duplicate.readerIndex() + "|" + buf.writerIndex());
        // duplicate 写的是 100，writerIndex = 3
        duplicate.writeByte(100);
        // buf 写的是 90，writerIndex = 3
        buf.writeByte(90);
        System.out.println("期望值是 100，实际上是：" + duplicate.readByte() + " 之所以不符合期望，是因为与 buf 相同的 writerIndex 覆盖了共用的内存块数据 ");
    }

}
