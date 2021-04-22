package com.wang.netty.daxin.chapter04netty;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

/**
 * ChannelInboundHandlerAdapter: 入站方法集合
 * ChannelOutboundHandlerAdapter: 出站方法集合
 * ChannelDuplexHandler: 出入站 方法集合
 *
 * @description: 自定义
 * @author: wei·man cui
 * @date: 2021/4/22 16:57
 */
public class SecondHandler extends ChannelDuplexHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("SecondHandler " + msg);
        // ctx.channel().write("I received your msg.");
        // ctx.channel().flush();
        ctx.channel().writeAndFlush("I received your msg.");
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        // 加入了 StringEncoder 之后：
        ctx.write(msg);
       /* final ByteBuf byteBuf = ctx.alloc().buffer(32);
        String data = (String) msg;
        byteBuf.writeBytes(data.getBytes(StandardCharsets.UTF_8));
        ctx.write(byteBuf);*/
    }
}
