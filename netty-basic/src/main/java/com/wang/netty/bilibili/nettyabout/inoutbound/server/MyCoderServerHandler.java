package com.wang.netty.bilibili.nettyabout.inoutbound.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description: 自定义 业务处理器
 * @author: wei·man cui
 * @date: 2021/2/25 17:47
 */
public class MyCoderServerHandler extends SimpleChannelInboundHandler<Long> {

    /**
     * MyByteToLongDecoder 对入站的信息 解码后，将数据传递给 本 业务处理器
     * 相应客户端一个消息
     *
     * @param channelHandlerContext 上下文
     * @param aLong                 入站信息
     * @throws Exception 异常
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Long aLong) throws Exception {
        System.out.println("[MyCoderServerHandler.channelRead0] 消息读取 被调用");
        System.out.println("从客户端 " + channelHandlerContext.channel().remoteAddress() + " 读取到Long： " + aLong);
        channelHandlerContext.writeAndFlush(98765L);
    }


    /**
     * 发生异常，关闭通道
     *
     * @param ctx   上下文
     * @param cause 原因
     * @throws Exception 异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
