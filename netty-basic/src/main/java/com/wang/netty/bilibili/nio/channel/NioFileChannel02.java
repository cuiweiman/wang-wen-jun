package com.wang.netty.bilibili.nio.channel;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 示例场景：
 * 从file01.txt中，读取内容 到控制台上；
 * 实际过程：
 * String ——> Buffer ——> Channel ——> file01.txt
 *
 * @author: wei·man cui
 * @date: 2021/2/21 23:22
 */
public class NioFileChannel02 {
    public static void main(String[] args) throws Exception {
        // 创建 文件 读取的 channel通道
        File file = new File("e:\\file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel channel = fileInputStream.getChannel();

        //创建 Buffer 缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
        channel.read(byteBuffer);
        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();
    }
}
