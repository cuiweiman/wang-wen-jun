package com.wang.netty.bilibili.nio.zerocopy;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * @description: 阻塞IO的 文件传输
 * @author: wei·man cui
 * @date: 2021/2/23 11:03
 */
public class BioClient {

    public static void main(String[] args) throws IOException {
        final Socket socket = new Socket("127.0.0.1", 7001);
        String path = "F:/电子书/MySQL技术内幕-第5版.pdf";
        final InputStream inputStream = new FileInputStream(path);

        final DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        byte[] bytes = new byte[2048];
        long read;
        long total = 0;

        long startTime = System.currentTimeMillis();

        while ((read = inputStream.read(bytes)) >= 0) {
            total += read;
            dataOutputStream.write(bytes);
        }

        System.out.println("发送总字节数： " + total + ", 耗时： " + (System.currentTimeMillis() - startTime));

        dataOutputStream.close();
        socket.close();
        inputStream.close();
    }

}
