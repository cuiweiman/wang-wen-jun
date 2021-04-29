package com.wang.netty.daxin.chapter12.boundhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 入站
 * @author: wei·man cui
 * @date: 2021/4/29 13:54
 */
@Slf4j
public class InHandlerA extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("[InHandlerA#channelRead] {}", msg.toString());
        super.channelRead(ctx, msg);
    }
}
