package com.wang.netty.daxin.chapter08idlestate.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 随便一个处理器
 * @author: wei·man cui
 * @date: 2021/4/28 15:28
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info("[NettyServerHandler#channelRead0] 读到消息：{}", msg);
    }
}
