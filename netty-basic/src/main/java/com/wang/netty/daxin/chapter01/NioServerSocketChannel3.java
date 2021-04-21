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
 * 最终优化结果：
 * 1. ServerSocketChannel 向 selector 注册 监听 客户端的 连接事件
 * 2. {@link EventLoopGroup} 管理 EventLoop 线程，负责 将 SocketChannel 的 读写事件 注册到 selector 中。
 * 3. {@link EventLoop2} 监听到 读写事件后，将 数据 分发给 {@link MyChannel}处理。
 * 4. {@link MyChannel} 中的 Pipeline 维护了 链式处理器，最终数据交由 链式处理器中的 各个处理器节点一次处理。
 *
 * @description: EventLoopGroup 管理多个 EventLoop
 * @author: wei·man cui
 * @date: 2021/4/21 13:28
 */
public class NioServerSocketChannel3 {

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

            EventLoopGroup eventLoopGroup = new EventLoopGroup();

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
                            eventLoopGroup.register(socketChannel, SelectionKey.OP_READ);
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
