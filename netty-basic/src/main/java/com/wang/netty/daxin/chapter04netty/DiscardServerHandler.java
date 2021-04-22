package com.wang.netty.daxin.chapter04netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @description: 官网案例：https://netty.io/wiki/user-guide-for-4.x.html
 * @author: wei·man cui
 * @date: 2021/4/22 16:32
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 加入了 StringDecoder 之后：
        String data = (String) msg;
        System.out.println("DiscardServerHandler " + data);
        ctx.fireChannelRead(data);

       /* ByteBuf buf = (ByteBuf) msg;
        try {
            String data = buf.toString(StandardCharsets.UTF_8);
            System.out.println("DiscardServerHandler " + data);
            ctx.fireChannelRead(data);
        } finally {
            buf.release();
        }*/
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

}
