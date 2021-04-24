package com.wang.netty.daxin.chapter04netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @description:
 * @date: 2021/4/24 9:43
 * @author: wei·man cui
 */
public class TcpClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[客户端]通道就绪");

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < 100; i++) {
            sb.append("Hei girl, I miss you.");
        }
        sb.append("]");
//        sb.append("]\r\n");
        ctx.writeAndFlush(Unpooled.copiedBuffer(sb.toString(), CharsetUtil.UTF_8));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("[客户端]接收响应信息：" + msg.toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
