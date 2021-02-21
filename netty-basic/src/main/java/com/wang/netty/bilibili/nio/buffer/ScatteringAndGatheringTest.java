package com.wang.netty.bilibili.nio.buffer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @description: Scattering：将数据写入到Buffer时，可以采用Buffer数组，依次写入。
 * Gathering：分散；从Buffer读出数据时，可以采用Buffer数组，依次读出。
 * @author: wei·man cui
 * @date: 2021/2/21 23:18
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 设定端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);
        // channel绑定端口
        serverSocketChannel.socket().bind(inetSocketAddress);
        // 创建Buffer 数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);
        // 等待客户端连接（telnet）
        SocketChannel socketChannel = serverSocketChannel.accept();
        // 从客户端接收 5+3=8 个字节
        int msgLength = 8;
        // 循环读取
        while (true) {
            // 记录 读取的字节数
            int byteRead = 0;
            while (byteRead < msgLength) {
                long length = socketChannel.read(byteBuffers);
                byteRead += length;
                System.out.println("byteRead = " + byteRead);
                // 流打印
                Arrays.asList(byteBuffers).stream()
                        .map(buffer -> "position=" + buffer.position() +
                                ", limit=" + buffer.limit()).forEach(System.out::println);
            }
            // 将所有的buffer进行flip反转
            Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.flip());
            // 将数据读出，显示到客户端
            long byteWrite = 0;
            while (byteWrite < msgLength) {
                long length = socketChannel.write(byteBuffers);
                byteWrite += length;
            }
            //清空所有的buffer
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.clear());
            System.out.println("byteRead = " + byteRead + "; byteWrute = " + byteWrite + "; msgLength = " + msgLength);
        }
    }
}
