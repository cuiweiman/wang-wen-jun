package com.wang.netty.bilibili.nio.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @description: NIO 实现 零拷贝
 * @author: wei·man cui
 * @date: 2021/2/23 11:17
 */
public class NioClient {
    public static void main(String[] args) {
        try (SocketChannel socketChannel = SocketChannel.open();
             FileChannel fileChannel = new FileInputStream("F:/电子书/MySQL技术内幕-第5版.pdf").getChannel()) {
            socketChannel.connect(new InetSocketAddress("localhost", 7003));

            long startTime = System.currentTimeMillis();

            System.out.println("开始传输文件");

            // 文件太大时，需要循环传输
            //在 linux下一个transferTo 方法就可以完成传输
            //在 windows 下 一次调用 transferTo 只能发送 8m , 需要分段传输文件
            int position = 0;
            long size = fileChannel.size();
            while (0 < size) {
                long transferCount = fileChannel.transferTo(position, size, socketChannel);
                if (transferCount > 0) {
                    position += transferCount;
                    size -= transferCount;
                }
            }

            System.out.println("发送的总的字节数 =" + position + " 耗时:" + (System.currentTimeMillis() - startTime));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
