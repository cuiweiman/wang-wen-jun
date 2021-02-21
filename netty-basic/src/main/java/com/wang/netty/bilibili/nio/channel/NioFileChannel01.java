package com.wang.netty.bilibili.nio.channel;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 示例场景：
 * 1. 使用ByteBuffer缓冲区和FileChannel通道，将Hello World 写入file01.txt中；
 * 2. 若文件不存在就创建。
 * 实际过程：
 * String ——> Buffer ——> Channel ——> file01.txt
 *
 * @author: wei·man cui
 * @date: 2021/2/21 23:21
 */
public class NioFileChannel01 {
    public static void main(String[] args) throws Exception {
        String msg = "Hello World";

        // 创建一个缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(msg.getBytes());
        // 数据写入缓冲区结束，转变为读。
        byteBuffer.flip();

        FileOutputStream fileOutputStream = new FileOutputStream("e:\\file01.txt");
        FileChannel fileChannel = fileOutputStream.getChannel();
        // 缓冲区的数据 写入 到 Channel
        fileChannel.write(byteBuffer);
        fileOutputStream.close();
    }
}
