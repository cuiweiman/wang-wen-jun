package com.wang.netty.bilibili.nettyabout.groupchat.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description:
 * @author: wei·man cui
 * @date: 2021/2/24 17:49
 */
public class NettyGroupChatHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

    }
}
