package com.wang.netty.daxin.chapter12.boundhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 出站
 * @author: wei·man cui
 * @date: 2021/4/29 13:54
 */
@Slf4j
public class OutHandlerB extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        log.debug("[OutHandlerB#channelRead] {}", msg.toString());
        super.write(ctx, msg, promise);
    }
}
