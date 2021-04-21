package com.wang.netty.daxin.chapter02.handler;

import java.nio.ByteBuffer;

/**
 * @description:
 * @date: 2021/4/22 1:56
 * @author: wei·man cui
 */
public class NioHandler2 implements NioHandler {
    @Override
    public void channelRead(NioHandlerContext ctx, Object msg) {
        ByteBuffer buffer = (ByteBuffer) msg;
        System.out.println("NioHandler2 " + new String(buffer.array()));
        // ctx.fireChannelRead(msg);

        // 发送数据回执时： MyHandler2-->MyChannel#doWrite()-->HandlerContext#write()-->MyHandler2#write
        ctx.getChannel().doWrite("I have received your msg.");
        ctx.flush();
    }

    @Override
    public void write(NioHandlerContext ctx, Object msg) {
        System.out.println("NioHandler2#write " + msg.toString());
        ctx.write(msg);
    }

    @Override
    public void flush(NioHandlerContext ctx) {
        System.out.println("NioHandler2#flush");
        ctx.flush();
    }
}
