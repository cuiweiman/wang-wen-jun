package com.wang.netty.bilibili.nio.channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 示例场景：
 * 使用一个Buffer完成数据在文件中的写入和读取.
 * 文件 ——> channel ——> Buffer ——> Channel ——> 文件copy
 *
 * @author: wei·man cui
 * @date: 2021/2/21 23:22
 */
public class NioFileChannel03 {
    public static void main(String[] args) throws Exception {
        // 读取file01文件的内容 到 channel中，再读取到 Buffer 中
        File file = new File("e:\\file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileChannel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        // 将 Buffer 中的数据流 写入channel，再通过channel写入到拷贝的文件中。
        FileOutputStream fileOutputStream = new FileOutputStream("e:\\file01_copy.txt");
        FileChannel writeFileChannel = fileOutputStream.getChannel();

        /*fileChannel.read(byteBuffer);
        byteBuffer.flip();
        writeFileChannel.write(byteBuffer);*/

        // 通过buffer，从两个 channel 中，进行数据读写。
        while (true) {
            byteBuffer.clear();
            // 读取channel
            int read = fileChannel.read(byteBuffer);
            if (read == -1) {
                break;
            }
            byteBuffer.flip();
            writeFileChannel.write(byteBuffer);
        }
        fileInputStream.close();
        fileOutputStream.close();
    }
}
