package com.wang.netty.daxin.chapter01;

import com.wang.netty.utils.ExecutorUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description: ServerSocket 服务器 端 套接字
 * @author: wei·man cui
 * @date: 2021/4/20 16:23
 */
public class BioServerSocket {

    public static void main(String[] args) throws IOException {
        ThreadPoolExecutor executor = ExecutorUtils.getExecutor();
        ServerSocket serverSocket = new ServerSocket(9090);
        while (true) {
            System.out.println("[服务端]等待客户端连接……");
            // 监听 客户端 连接，若没有连接，线程会 阻塞 在这里
            final Socket socket = serverSocket.accept();
            System.out.println("[服务端]客户端连接成功……" + socket.getRemoteSocketAddress());

            executor.execute(new ClientHandler(socket));
        }
    }

    public static class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            int readNum = 0;
            byte[] content = new byte[1024];
            try (InputStream inputStream = socket.getInputStream();
                 OutputStream outputStream = socket.getOutputStream()) {
                while ((readNum = inputStream.read(content)) != -1) {
                    System.out.println(new String(Arrays.copyOf(content, readNum)));
                    /*
                     * 写回 二进制数据 时，数据交给 操作系统，操作系统封装成 TCP数据包，通过网络传输。 接收到后，拆解和校验TCP数据包。
                     */
                    outputStream.write("server get it".getBytes(StandardCharsets.UTF_8));
                }
                socket.close();
            } catch (IOException e) {
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                throw new RuntimeException(e);
            }


        }
    }

}
