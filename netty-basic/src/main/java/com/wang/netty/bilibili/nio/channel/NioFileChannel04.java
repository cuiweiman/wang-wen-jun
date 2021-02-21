package com.wang.netty.bilibili.nio.channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * 示例场景：
 * 使用channel.transferFrom()方法，拷贝图片
 *
 * @author: wei·man cui
 * @date: 2021/2/21 23:22
 */
public class NioFileChannel04 {
    public static void main(String[] args) throws Exception {
        File file = new File("e:\\1.jpg");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel inputChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("e:\\1_copy.jpg");
        FileChannel outputChannel = fileOutputStream.getChannel();

        inputChannel.transferTo(0, inputChannel.size(), outputChannel);
        //outputChannel.transferFrom(inputChannel,0,inputChannel.size());

        fileInputStream.close();
        fileOutputStream.close();
    }
}
