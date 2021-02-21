package com.wang.netty.bilibili.bio;

import com.wang.netty.utils.ExecutorUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description: 阻塞IO，即传统IO 的通讯：客户端
 * @author: wei·man cui
 * @date: 2021/2/21 17:50
 */
public class BioClient {

    public static void main(String[] args) {

        ThreadPoolExecutor executor = ExecutorUtils.getExecutor();
        for (int i = 0; i < 5; i++) {
            executor.execute(() -> {
                try {
                    Socket socket = new Socket("127.0.0.1", 6666);
                    clientHandler(socket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static void clientHandler(Socket socket) {
        try (OutputStream outputStream = socket.getOutputStream();
             Writer writer = new OutputStreamWriter(outputStream, "UTF-8")) {
            writer.write("你好世界");
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
