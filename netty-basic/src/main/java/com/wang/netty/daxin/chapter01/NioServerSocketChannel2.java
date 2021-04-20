package com.wang.netty.daxin.chapter01;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

/**
 * 优化：使用一个 selector 处理客户端的连接；
 * 在使用 一个 selector 监听 客户端通道中的 读/写事件。
 * <p>
 * 即 一个 selector 监听 acceptable 事件；
 * 使用另一个 selector 监听 readable 事件；
 *
 * @description: 非阻塞IO的  ServerSocketChannel
 * @author: wei·man cui
 * @date: 2021/4/20 17:40
 */
public class NioServerSocketChannel2 {

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

            EventLoop eventLoop = new EventLoop();

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
                            System.out.println("[服务端]接收到客户端连接: " + socketChannel.getRemoteAddress());
                            // 并对客户端的套接字 进行 写事件监听
                            socketChannel.configureBlocking(false);
                            // 注册时 读事件 也需要注册到 EventLoop 中
                            eventLoop.register(socketChannel, SelectionKey.OP_READ);
                        }
                        iterator.remove();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
