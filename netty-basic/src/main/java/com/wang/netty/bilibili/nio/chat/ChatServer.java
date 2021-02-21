package com.wang.netty.bilibili.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 聊天系统 服务器端
 * channel.write(); 缓冲区——>通道。
 * channel.read(); 通道——>缓冲区
 *
 * @author: wei·man cui
 * @date: 2021/2/21 23:28
 */
public class ChatServer {
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private final int PORT = 6667;

    public ChatServer() {
        try {
            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            //设置 非阻塞
            listenChannel.configureBlocking(false);
            selector = Selector.open();
            // 注册通道，并监听 客户端 通道连接事件
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("【服务器】服务端启动完成，等待连接……");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listen() {
        try {
            while (true) {
                int count = selector.select();
                if (count > 0) {
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        if (key.isAcceptable()) {
                            // 接收客户端的通道连接
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            sc.register(selector, SelectionKey.OP_READ);
                            System.out.println("【服务器】客户端上线：" + sc.getRemoteAddress());
                        } else if (key.isReadable()) {
                            // 通道是可读状态，将通道数据读到缓冲区
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

    /**
     * 服务端 读取 客户端发送的消息
     *
     * @param key
     */
    private void readData(SelectionKey key) {
        SocketChannel channel = null;
        try {
            channel = (SocketChannel) key.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int count = channel.read(byteBuffer);
            if (count > 0) {
                // 将缓冲区的数据转换成字符串
                String msg = new String(byteBuffer.array());
                System.out.println("【服务器】接收到客户端消息：" + msg);
                // 向其他客户端转发消息
                sendMsgToOtherClients(msg, channel);
            }
        } catch (IOException e) {
            // 客户端下线。
            try {
                System.out.println("【服务器】客户端下线：" + channel.getRemoteAddress());
                // 取消注册，关闭通道
                key.cancel();
                channel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void sendMsgToOtherClients(String msg, SocketChannel selfChannel) {
        try {
            Set<SelectionKey> keySet = selector.keys();
            for (SelectionKey key : keySet) {
                Channel channel = key.channel();
                // 排除 服务端的ServerSocketChannel 和 发送信息的客户端的SocketChannel
                // channel是客户端通道，并且不是发送信息的那个客户端
                if (channel instanceof SocketChannel && channel != selfChannel) {
                    ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
                    // 写入 通道
                    SocketChannel targetChannel = (SocketChannel) channel;
                    System.out.println("【服务器】转发消息给：" + targetChannel.getRemoteAddress());
                    targetChannel.write(byteBuffer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 创建一个服务器对象，并启动通道监听
        ChatServer chatServer = new ChatServer();
        chatServer.listen();
    }

}
