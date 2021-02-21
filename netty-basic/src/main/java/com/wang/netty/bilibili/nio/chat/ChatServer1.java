package com.wang.netty.bilibili.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 群聊系统 服务器端
 *
 * @author: wei·man cui
 * @date: 2021/2/21 23:29
 */
public class ChatServer1 {

    private Integer PORT = 9999;
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    public ChatServer1() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false);

            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("【服务器】启动成功，等待连接……");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        System.out.println("监听线程：" + Thread.currentThread().getName());
        try {
            while (true) {
                int count = selector.select();
                if (count > 0) {
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        if (key.isAcceptable()) {
                            // 接收 客户端的 通道连接
                            SocketChannel channel = serverSocketChannel.accept();
                            channel.configureBlocking(false);
                            channel.register(selector, SelectionKey.OP_READ);
                            System.out.println("【服务器】客户端连接成功:" + channel.getRemoteAddress().toString());
                        } else if (key.isReadable()) {
                            // 读取 客户端 的 数据
                            readData(key);
                        }
                        keyIterator.remove();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readData(SelectionKey key) {
        SocketChannel socketChannel = null;
        try {
            socketChannel = (SocketChannel) key.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int readCount = socketChannel.read(byteBuffer);
            if (readCount > 0) {
                String msg = new String(byteBuffer.array());
                System.out.println("【服务器】接收到消息：" + msg);
                sendOut(msg, socketChannel);
            }
        } catch (IOException e) {
            try {
                System.out.println("【服务器】客户端下线：" + socketChannel.getRemoteAddress().toString());
                key.cancel();
                socketChannel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    public void sendOut(String msg, SocketChannel self) {
        System.out.println("服务器转发消息线程：" + Thread.currentThread().getName());
        try {
            Set<SelectionKey> keySet = selector.keys();
            for (SelectionKey key : keySet) {
                Channel channel = key.channel();
                if (channel instanceof SocketChannel && channel != self) {
                    // channel是客户端通道，并且不是发送信息的那个客户端
                    SocketChannel target = (SocketChannel) channel;
                    ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
                    target.write(byteBuffer);
                    System.out.println("【服务器】转发信息成功");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ChatServer1 chatServer1 = new ChatServer1();
        chatServer1.listen();
    }

}
