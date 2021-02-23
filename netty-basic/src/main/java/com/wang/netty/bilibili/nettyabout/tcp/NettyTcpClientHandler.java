package com.wang.netty.bilibili.nettyabout.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @description:
 * @author: wei·man cui
 * @date: 2021/2/23 15:55
 */
public class NettyTcpClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[客户端]通道就绪");
        ctx.writeAndFlush(Unpooled.copiedBuffer("[有内鬼，终止交易]", CharsetUtil.UTF_8));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        String message = byteBuf.toString(CharsetUtil.UTF_8);
        System.out.println("[客户端]服务端地址：" + ctx.channel().remoteAddress() + "，服务端回复信息：" + message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
