package com.wang.netty.daxin.chapter01;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * @description: 非阻塞IO的  ServerSocketChannel
 * @author: wei·man cui
 * @date: 2021/4/20 17:40
 */
public class NioServerSocketChannel {

    public static void main(String[] args) {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress(9090));
            // 设置 非阻塞模式
            serverSocketChannel.configureBlocking(false);

            // 创建 事件监听器
            Selector selector = SelectorProvider.provider().openSelector();
            // 向 事件监听器 注册 accept 事件的监听
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("[服务端]服务端启动成功，等待连接……");

            // 遍历所有的 监听事件，发生事件时做出相应
            while (true) {
                // 发生 IO 事件（OP_ACCEPT事件）的个数
                int eventNum = selector.select();
                if (eventNum > 0) {
                    final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        final SelectionKey key = iterator.next();
                        if (key.isAcceptable()) {
                            // 获取到连接的 客户端 套接字
                            final SocketChannel socketChannel = serverSocketChannel.accept();
                            // 并对客户端的套接字 进行 写事件监听
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            System.out.println("[服务端]接收到客户端连接: " + socketChannel.getRemoteAddress());
                        } else if (key.isReadable()) {
                            // 获取到 客户端 的 数据写入 事件
                            readClient(key);
                        }
                        iterator.remove();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void readClient(SelectionKey selectionKey) {
        SocketChannel channel = null;
        try {
            channel = (SocketChannel) selectionKey.channel();
            final ByteBuffer buffer = ByteBuffer.allocate(1024);
            final int read = channel.read(buffer);
            if (read > 0) {
                String msg = new String(buffer.array());
                System.out.println("[服务端]接收到客户端消息：" + msg);
            }
        } catch (IOException e) {
            try {
                selectionKey.cancel();
                channel.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    private static void respClient(String msg, SocketChannel channel) {
        try {
            final ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8));
            final int write = channel.write(buffer);
            System.out.println("[服务端]相应客户端数据发送结束：write length=" + write);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
