package com.wang.netty.bilibili.nettyabout.inoutbound.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description: 处理 业务逻辑
 * @author: wei·man cui
 * @date: 2021/2/25 17:50
 */
public class MyClientHandler extends SimpleChannelInboundHandler<Long> {

    /**
     * 本案例 客户端读取服务端的消息，并回复
     *
     * @param channelHandlerContext 上下文
     * @param aLong                 入站信息
     * @throws Exception 异常
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Long aLong) throws Exception {
        System.out.println("[客户端]消息读取完成：" + aLong);
    }

    /**
     * 向服务器端 发送数据。
     * 会先经过 MyClientHandler，在经过MyLongToByteEncoder编码器。
     *
     * @param ctx 上下文
     * @throws Exception 异常
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[MyClientHandler.channelActive]发送数据");

        // 会调用 多次 decode，直到buffer中没有可读字节。
        // 由于 传递的消息不符合 编码 类型，因此不会进行编码。
        // 但服务器端的解码器收到的是 二进制码，并不直到消息不是Long类型的，因此仍然会进行解码
        // ctx.writeAndFlush(Unpooled.copiedBuffer("AbCdAbCdAbCdAbCd", CharsetUtil.UTF_8));

        ctx.writeAndFlush(123456L);
    }
}
