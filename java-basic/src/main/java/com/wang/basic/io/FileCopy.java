package com.wang.basic.io;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @description: 文件拷贝
 * @author: wei·man cui
 * @date: 2021/2/5 10:04
 */
public class FileCopy {

    public static void main(String[] args) {
        String sourcePath = "E:/test.txt";
        String targetPath = "E:/test_copied.txt";
        File source = new File(sourcePath);
        File target = new File(targetPath);

        // copyFileByStream(source, target);
        // copyFileByNioFilesCopy(source, target);
        copyFileByNio(source, target);
    }

    /**
     * 传统 IO
     * 通过 文件流 拷贝
     *
     * @param source 源
     * @param target 目标
     */
    public static void copyFileByStream(File source, File target) {
        try (final InputStream inputStream = new FileInputStream(source);
             OutputStream outputStream = new FileOutputStream(target)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > -1) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * NIO 工具类
     * {@link java.nio.file.Files#copy(java.nio.file.Path, java.io.OutputStream)}方法拷贝
     *
     * @param source 源
     * @param target 目标
     */
    public static void copyFileByNioFilesCopy(File source, File target) {
        try {
            Files.copy(Paths.get(source.getPath()), new FileOutputStream(target));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用 NIO 的 Channel 和 ByteBuffer 拷贝
     *
     * @param source 源
     * @param target 目标
     */
    public static void copyFileByNio(File source, File target) {
        try (FileInputStream in = new FileInputStream(source);
             FileOutputStream out = new FileOutputStream(target)) {
            FileChannel inChannel = in.getChannel();
            FileChannel outChannel = out.getChannel();
            // 设置缓冲区大小为 1M（每次即可读取 1M）
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 1024);
            while ((inChannel.read(byteBuffer)) > -1) {
                // 切换读写模式
                byteBuffer.flip();
                outChannel.write(byteBuffer);
                // 清空缓冲区，循环读
                byteBuffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
