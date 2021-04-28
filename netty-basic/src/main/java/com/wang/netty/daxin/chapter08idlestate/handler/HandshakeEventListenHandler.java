package com.wang.netty.daxin.chapter08idlestate.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.concurrent.TimeUnit;

/**
 * @description: 握手事件监听 处理器
 * @author: wei·man cui
 * @date: 2021/4/28 16:19
 */
public class HandshakeEventListenHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            // 握手成功后，向 channel 添加 定时任务：10s后启动，每隔5s执行一次 Runnable 任务
            final ScheduledFuture<?> scheduledFuture = ctx.channel().eventLoop().scheduleAtFixedRate(() -> {
                ctx.channel().writeAndFlush(new PingWebSocketFrame());
            }, 10, 5, TimeUnit.SECONDS);

            // 当 连接 关闭时，移除 定时任务
            ctx.channel().closeFuture().addListener(channelFuture -> {
                scheduledFuture.cancel(true);
            });
        } else {
            ctx.fireChannelRead(evt);
        }

    }
}
