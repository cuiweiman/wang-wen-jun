package com.wang.netty.bilibili.nio.zerocopy;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @description: NIO 实现 零拷贝
 * @author: wei·man cui
 * @date: 2021/2/23 11:17
 */
public class NioServer {
    public static void main(String[] args) {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.socket().bind(new InetSocketAddress(7003));
            while (true) {
                serverSocketChannel.configureBlocking(true);
                SocketChannel acceptChannel = serverSocketChannel.accept();
                System.out.println("服务端接收到连接");

                FileOutputStream outputStream = new FileOutputStream("F:/电子书/MySQL技术内幕-第5版-bak.pdf");
                FileChannel fileChannel = outputStream.getChannel();
                ByteBuffer buffer = ByteBuffer.allocate(2048);
                int read = 0;
                while ((read = acceptChannel.read(buffer)) != -1) {
                    buffer.clear();
                    fileChannel.write(buffer);
                    // 从头开始读
                    buffer.flip();
                }
                // 关闭文件通道
                fileChannel.close();
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
