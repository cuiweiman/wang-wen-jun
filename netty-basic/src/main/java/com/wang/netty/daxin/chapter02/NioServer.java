package com.wang.netty.daxin.chapter02;

import com.wang.netty.daxin.chapter02.eventloop.NioEventLoopGroup;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

/**
 * 监听客户端连接；
 * 读写事件交由 {@link NioEventLoopGroup} 管理
 *
 * @description: 服务端
 * @author: wei·man cui
 * @date: 2021/4/21 17:58
 */
public class NioServer {

    public static void main(String[] args) {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress(9090));
            // 配置 非阻塞 属性
            serverSocketChannel.configureBlocking(false);

            // 注册 监听 客户端的连接 事件，在这里 只处理 连接事件
            final Selector selector = SelectorProvider.provider().openSelector();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("[服务端]服务器启动成功，等待连接……");

            NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
            while (true) {
                final int select = selector.select();
                // 发生了 IO 事件，OP_ACCEPT/OP_READ/OP_WRITE/OP_CONNECT
                if (select > 0) {
                    final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        final SelectionKey selectionKey = iterator.next();
                        if (selectionKey.isAcceptable()) {
                            final SocketChannel channel = serverSocketChannel.accept();
                            System.out.println("[服务端]接收到客户端连接：" + channel.getRemoteAddress());
                            channel.configureBlocking(false);
                            eventLoopGroup.register(channel, SelectionKey.OP_READ);
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
