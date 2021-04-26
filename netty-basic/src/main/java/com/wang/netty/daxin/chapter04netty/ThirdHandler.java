package com.wang.netty.daxin.chapter04netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * SimpleChannelInboundHandler 和 ChannelInboundHandlerAdapter 的区别：
 * 1. SimpleChannelInboundHandler：自动释放 ByteBuf 的内存
 * 2. SimpleChannelInboundHandler：使用泛型避免了强制类型转换
 *
 * @description:
 * @author: wei·man cui
 * @date: 2021/4/26 15:08
 */
public class ThirdHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("[ThirdHandler#channelRead0] I received your msg." + msg);
        ctx.channel().writeAndFlush("[ThirdHandler#channelRead] I received your msg.");
    }



}
