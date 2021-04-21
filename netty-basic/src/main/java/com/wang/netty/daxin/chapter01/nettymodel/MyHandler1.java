package com.wang.netty.daxin.chapter01.nettymodel;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @description:
 * @author: wei·man cui
 * @date: 2021/4/21 16:08
 */
public class MyHandler1 implements Handler {
    @Override
    public void channelRead(HandlerContext ctx, Object msg) {
        ByteBuffer buffer = (ByteBuffer) msg;
        System.out.println("MyHandler1 " + new String(buffer.array()));
        ctx.fireChannelRead(msg);
    }

    @Override
    public void write(HandlerContext ctx, Object msg) {
        System.out.println("MyHandler1 write");
        ByteBuffer buffer = ByteBuffer.wrap(msg.toString().getBytes(StandardCharsets.UTF_8));
        ctx.write(buffer);
    }

    @Override
    public void flush(HandlerContext ctx) {
        System.out.println("MyHandler1 flush");
        ctx.flush();
    }
}
