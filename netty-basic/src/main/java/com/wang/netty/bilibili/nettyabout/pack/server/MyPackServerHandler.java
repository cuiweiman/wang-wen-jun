package com.wang.netty.bilibili.nettyabout.pack.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.UUID;

/**
 * @description: 模拟 TCP 数据传输的 粘包与拆包问题。服务端 自定义业务处理器。
 * @author: wei·man cui
 * @date: 2021/2/25 17:53
 */
public class MyPackServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private int count = 0;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        // 创建一个 字节数组， 长度为 byteBuf中字节的长度
        byte[] bytes = new byte[byteBuf.readableBytes()];

        // 读取和接收消息
        byteBuf.readBytes(bytes);
        String msg = new String(bytes, CharsetUtil.UTF_8);
        System.out.println("[服务端]接收到数据：" + msg);
        System.out.println("[服务端]接收de消息数量：" + (++this.count));

        // 回复消息
        ByteBuf response = Unpooled.copiedBuffer(UUID.randomUUID().toString() + "\n", CharsetUtil.UTF_8);
        channelHandlerContext.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
