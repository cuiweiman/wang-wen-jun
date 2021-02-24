package com.wang.netty.bilibili.nettyabout.bufunpooled;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

/**
 * @description: Netty 的 ByteBuf 缓冲区
 * @author: wei·man cui
 * @date: 2021/2/24 17:05
 */
public class ByteBuf01 {
    public static void main(String[] args) {
        ByteBuf byteBuf1 = Unpooled.buffer(10);
        for (int i = 0; i < 6; i++) {
            byteBuf1.writeByte(i);
        }
        printByteBuf(byteBuf1);

        for (int i = 0; i < 3; i++) {
            System.out.println(byteBuf1.readByte());
        }
        printByteBuf(byteBuf1);


        ByteBuf byteBuf2 = Unpooled.copiedBuffer("猴赛雷呀", CharsetUtil.UTF_8);
        final String string = byteBuf2.toString(CharsetUtil.UTF_8);
        System.out.println(string);
        printByteBuf(byteBuf2);

    }

    private static void printByteBuf(ByteBuf byteBuf) {
        System.out.println("=========================");
        System.out.println("|  写标：" + byteBuf.writerIndex());
        System.out.println("|  读标：" + byteBuf.readerIndex());
        System.out.println("|  容量：" + byteBuf.capacity());
        System.out.println("————————————————————————");
    }

}
