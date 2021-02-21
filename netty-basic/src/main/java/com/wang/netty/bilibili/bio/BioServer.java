package com.wang.netty.bilibili.bio;

import com.wang.netty.utils.ExecutorUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description: 阻塞IO，即传统IO 的通讯：服务端
 * @author: wei·man cui
 * @date: 2021/2/21 17:50
 */
public class BioServer {

    public static void main(String[] args) throws IOException {
        ThreadPoolExecutor executor = ExecutorUtils.getExecutor();
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动");
        while (true) {
            final Socket socket = serverSocket.accept();
            System.out.println("服务器连接成功，开始通讯");
            executor.execute(() -> {
                handler(socket);
            });
        }

    }

    public static void handler(Socket socket) {
        try {
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            //循环读取客户端的数据。
            while (true) {
                int read = inputStream.read(bytes);
                if (read != -1) {
                    //输出客户端发送的数据
                    System.out.println("线程Name= " + Thread.currentThread().getName() + "；"
                            + new String(bytes, 0, read));
                } else {
                    // 读取完成，结束循环
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
