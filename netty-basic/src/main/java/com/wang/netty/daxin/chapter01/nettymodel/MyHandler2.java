package com.wang.netty.daxin.chapter01.nettymodel;

import java.nio.ByteBuffer;

/**
 * @description:
 * @author: wei·man cui
 * @date: 2021/4/21 16:08
 */
public class MyHandler2 implements Handler {
    @Override
    public void channelRead(HandlerContext ctx, Object msg) {
        ByteBuffer buffer = (ByteBuffer) msg;
        System.out.println("MyHandler2 " + new String(buffer.array()));
        // ctx.fireChannelRead(msg);

        // 发送数据回执时： MyHandler2-->MyChannel#doWrite()-->HandlerContext#write()-->MyHandler2#write
        ctx.getMyChannel().doWrite("[服务端]数据接收回执");
        ctx.flush();
    }

    @Override
    public void write(HandlerContext ctx, Object msg) {
        System.out.println("MyHandler2#write " + msg.toString());
        ctx.write(msg);
    }

    @Override
    public void flush(HandlerContext ctx) {
        System.out.println("MyHandler2 flush");
        ctx.flush();
    }

}
