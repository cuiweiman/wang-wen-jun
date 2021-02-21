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
 * 群聊系统 客户端
 */
public class ChatClient1 {

    private String HOST = "127.0.0.1";
    private Integer PORT = 9999;
    private Selector selector;
    private SocketChannel socketChannel;

    public ChatClient1() {
        try {
            socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
            socketChannel.configureBlocking(false);

            selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_READ);
            System.out.println("【客户端】上线：" + socketChannel.getLocalAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readInfo() {
        try {
            int readChannel = selector.select();
            if (readChannel > 0) {
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        channel.read(byteBuffer);
                        String msg = new String(byteBuffer.array());
                        System.out.println("【客户端】收信：" + msg);
                    }
                    keyIterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendInfo(String msg) {
        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
            socketChannel.write(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ChatClient1 client1 = new ChatClient1();
        new Thread(() -> {
            try {
                while (true) {
                    client1.readInfo();
                    Thread.currentThread().sleep(3000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String msg = scanner.nextLine();
            client1.sendInfo(msg);
        }
    }

}






