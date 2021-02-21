package com.wang.netty.bilibili.nio.basic;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @description: 非阻塞IO 通讯服务端
 * @date: 2021/2/21 22:21
 * @author: wei·man cui
 */
public class NioServer {
    public static void main(String[] args) throws IOException {
        // 创建Channel通道，绑定本地端口：66666，并设置非阻塞模式, Channel 必须设置成 非阻塞，才能注册到 Selector
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        serverSocketChannel.configureBlocking(false);

        // 将通道注册到 Selector 中,关注事件为 服务端接收客户端连接事件：OP_ACCEPT
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环等待客户端连接
        while (true) {
            if (selector.select(1000) == 0) {
                // 等待 1s，selector中没有没有事件发生，事件指的是 OP_ACCEPT
                System.out.println("服务器等待了1s，无连接");
                continue;
            }
            // 否则，有事件发生，遍历所有的 SelectionKey ，并反向获取 Channel
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey selectionKey = keyIterator.next();
                if (selectionKey.isAcceptable()) {
                    // 发生 OP_ACCEPT 事件，表示存在客户端 向 服务端发起连接
                    // 获取 SocketChannel 通道，并注册到 Selector 中，并设置缓存区
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                } else if (selectionKey.isReadable()) {
                    // 发生 OP_READ 事件，表示可读。并获取通道关联的 ByteBuffer,将通道中的数据读入到 缓冲区中
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                    channel.read(byteBuffer);
                    System.out.println("from Client: " + new String(byteBuffer.array()));
                }
                keyIterator.remove();
            }
        }
    }
}
