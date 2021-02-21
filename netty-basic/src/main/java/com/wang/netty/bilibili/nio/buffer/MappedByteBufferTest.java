package com.wang.netty.bilibili.nio.buffer;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @description: 直接在 堆外的内存 中，直接 修改文件，即操作系统不需要将文件拷贝一次。
 * @date: 2021/2/21 23:17
 * @author: wei·man cui
 */
public class MappedByteBufferTest {
    public static void main(String[] args) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("e:\\file01.txt", "rw");

        FileChannel fileChannel = randomAccessFile.getChannel();
        /*
         * 参数1：使用读写模式；
         * 参数2: 0-可以直接修改的起始位置；
         * 参数3：5-映射到内存的大小，即将文件的多少个字节映射到内存中，那么就能直接修改
         * 即可以修改文件的 0-5 范围内的字节
         */
        MappedByteBuffer mappedByteBuffer =
                fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        mappedByteBuffer.put(0, (byte) 'A');
        mappedByteBuffer.put(3, (byte) 'X');
        randomAccessFile.close();
    }
}
