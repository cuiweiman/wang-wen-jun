package com.wang.netty.bilibili.nio.zerocopy;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @description: 阻塞IO的 文件传输
 * @author: wei·man cui
 * @date: 2021/2/23 11:04
 */
public class BioServer {
    public static void main(String[] args) throws IOException {
        final ServerSocket serverSocket = new ServerSocket(7001);
        while (true) {
            final Socket socket = serverSocket.accept();
            final DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            try (OutputStream outputStream = new FileOutputStream("F:/电子书/MySQL技术内幕-第5版-bak.pdf")) {
                byte[] bytes = new byte[2048];
                while (true) {
                    final int read = dataInputStream.read(bytes, 0, bytes.length);
                    if (read >= 0) {
                        outputStream.write(bytes, 0, bytes.length);
                    } else {
                        break;
                    }
                }
                outputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
