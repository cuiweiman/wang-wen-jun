package com.wang.netty.daxin.chapter10zerocopy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * 零拷贝：没有 内核态 到 用户态 的拷贝
 *
 * @description: 文件传输
 * @author: wei·man cui
 * @date: 2021/4/28 18:03
 */
public class FileTransferDemo {
    public static void main(String[] args) throws IOException {
        fileChannelTest();
    }

    /**
     * 内核态磁盘文件——>内核态TCP缓冲区
     *
     * @throws IOException 异常
     */
    public static void fileChannelTest() throws IOException {
        final RandomAccessFile file = new RandomAccessFile("E://imei.xlsx", "rw");
        final FileChannel inChannel = file.getChannel();

        final SocketChannel channel = SocketChannel.open();
        channel.bind(new InetSocketAddress(9090));
        // 直接把 文件字节 拷贝到  系统 TCP 缓冲区
        inChannel.transferTo(0, 100, channel);
    }

    /**
     * 常规做法：传输文件
     * <p>
     * 内核态磁盘文件——>用户态堆外内存——>内核态TCP缓冲区
     *
     * @throws IOException 异常
     */
    public static void tradition() throws IOException {
        final RandomAccessFile file = new RandomAccessFile("E://imei.xlsx", "rw");
        final FileChannel inChannel = file.getChannel();
        final ByteBuffer buffer = ByteBuffer.allocateDirect(48);
        inChannel.read(buffer);

        final SocketChannel channel = SocketChannel.open();
        channel.bind(new InetSocketAddress(9090));
        channel.write(buffer);
    }

}
