package com.wang.netty.bilibili.nio.basic;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @description: 非阻塞IO 通讯 客户端
 * @date: 2021/2/21 22:20
 * @author: wei·man cui
 */
public class NioClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 6666);
        // 连接 服务器
        if (!socketChannel.connect(socketAddress)) {
            // 连接服务器 失败
            while (!socketChannel.finishConnect()) {
                System.out.println("连接服务器 失败/需要时间，客户端不会阻塞，可以做其它任务");
            }
        }

        String msg = "我吹过你吹过的晚风，那我们算不算相拥，可如梦初醒般的两手空空";
        ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
        socketChannel.write(byteBuffer);
        System.in.read();
    }
}
