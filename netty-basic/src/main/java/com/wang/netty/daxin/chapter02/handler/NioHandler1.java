package com.wang.netty.daxin.chapter02.handler;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @description:
 * @date: 2021/4/22 1:56
 * @author: wei·man cui
 */
public class NioHandler1 implements NioHandler {
    @Override
    public void channelRead(NioHandlerContext ctx, Object msg) {
        ByteBuffer buffer = (ByteBuffer) msg;
        System.out.println("NioHandler1 " + new String(buffer.array()));
        ctx.fireChannelRead(msg);
    }

    @Override
    public void write(NioHandlerContext ctx, Object msg) {
        System.out.println("NioHandler1 write");
        ByteBuffer buffer = ByteBuffer.wrap(msg.toString().getBytes(StandardCharsets.UTF_8));
        ctx.write(buffer);
    }

    @Override
    public void flush(NioHandlerContext ctx) {
        System.out.println("NioHandler1 flush");
        ctx.flush();
    }
}
