package com.wang.netty.daxin.chapter07ws;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 同时配置成 无状态的 单例 共享 Handler
 *
 * @description: WebSocket 握手成功事件
 * @author: wei·man cui
 * @date: 2021/4/28 10:04
 */
@Slf4j
@ChannelHandler.Sharable
public class HandshakeEventHandler extends ChannelInboundHandlerAdapter {

    public static final HandshakeEventHandler INSTANCE = new HandshakeEventHandler();

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            // WebSocket 握手成功事件
            WebSocketServerProtocolHandler.HandshakeComplete event = (WebSocketServerProtocolHandler.HandshakeComplete) evt;
            log.info("握手成功事件 URI={}", event.requestUri());
            log.info("握手成功事件，头部信息={}", event.requestHeaders().toString());
        } else {
            // 其他事件 交由父类 处理
            super.userEventTriggered(ctx, evt);
        }
    }
}
