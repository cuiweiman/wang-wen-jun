package com.wang.nettyboot.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @author: wei·man cui
 * @date: 2021/4/30 11:09
 */
@Slf4j
public class SendMsgHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().writeAndFlush("SendMsgHandler#active");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("[SendMsgHandler#channelRead] 接收到信息：{}", msg.toString());
        ctx.channel().writeAndFlush("SendMsgHandler#read");
    }
}
