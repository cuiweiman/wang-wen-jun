package com.wang.netty.bilibili.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 聊天系统 客户端
 */
public class ChatClient {

    private final String HOST = "127.0.0.1";
    private final Integer PORT = 6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String userName;

    public ChatClient() {
        try {
            socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
            socketChannel.configureBlocking(false);

            selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_READ);

            userName = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println("【客户端】上线：" + socketChannel.getLocalAddress());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendInfo(String info) {
        try {
            info = "【" + userName + "】：" + info;
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readInfo() {
        try {
            // 若没有连接，selector.select会阻塞。
            int readChannel = selector.select();
            if (readChannel > 0) {
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        channel.read(byteBuffer);
                        String msg = new String(byteBuffer.array()).trim();
                        System.out.println("【客户端】收到信息：" + msg);
                    }
                    keyIterator.remove();
                }

            } else {
                //System.out.println("【客户端】暂无可用通道。");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 启动客户端
        ChatClient chatClient = new ChatClient();
        // 创建线程，每隔3秒，读取一次数据。
        new Thread(() -> {
            while (true) {
                chatClient.readInfo();
                try {
                    Thread.currentThread().sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        // 向服务器发送信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String msg = scanner.nextLine();
            chatClient.sendInfo(msg);
        }
    }

}
