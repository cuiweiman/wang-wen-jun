package com.wang.netty.bilibili.nettyabout.groupchat.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description:
 * @author: wei·man cui
 * @date: 2021/2/25 13:25
 */
public class NettyGroupChatClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("[客户端]接收到信息：" + msg);
    }
}
