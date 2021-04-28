package com.wang.netty.daxin.chapter10zerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @description: 内存零拷贝：堆外内存 与 堆内存
 * @author: wei·man cui
 * @date: 2021/4/28 17:53
 */
public class FileChannelDemo {

    public static void main(String[] args) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(48);
        final SocketChannel channel = SocketChannel.open();
        channel.bind(new InetSocketAddress(9090));
        channel.write(buffer);
        channel.read(buffer);
    }

}
