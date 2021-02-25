package com.wang.netty.bilibili.nettyabout.pack.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @description: 模拟 TCP 数据传输的 粘包与拆包问题，自定义 业务处理器。
 * @author: wei·man cui
 * @date: 2021/2/25 17:54
 */
public class MyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count = 0;

    /**
     * 客户端 一旦活动，就触发本函数
     * 发送 十个 包
     *
     * @param ctx 通道
     * @throws Exception 异常
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            ByteBuf byteBuf = Unpooled.copiedBuffer("Hello Server " + i, CharsetUtil.UTF_8);
            ctx.writeAndFlush(byteBuf);
        }

    }

    /**
     * 接收 服务端 的数据
     *
     * @param channelHandlerContext 通道
     * @param byteBuf               接收数据
     * @throws Exception 异常
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        String msg = new String(bytes, CharsetUtil.UTF_8);
        System.out.println("[客户端]接收消息：" + msg);
        System.out.println("[客户端]接收消息数量=" + (++this.count));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
