package com.wang.netty.daxin.chapter07ws;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: WebSocket 数据处理
 * @author: wei·man cui
 * @date: 2021/4/26 17:29
 */
@Slf4j
public class WebSocketTextHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        log.debug("[WebSocketTextHandler#channelRead0]读到数据：{}", msg.text());
        ctx.fireChannelRead(msg);
    }
}
