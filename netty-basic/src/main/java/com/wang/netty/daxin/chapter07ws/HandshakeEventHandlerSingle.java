package com.wang.netty.daxin.chapter07ws;

import com.wang.netty.daxin.chapter07ws.responsehandler.WsContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.Attribute;
import lombok.extern.slf4j.Slf4j;

/**
 * 同时配置成 无状态的 单例 共享 Handler
 * 点对点发送消息,需要将 用户ID 与 其客户端的 Channel 进行绑定
 * {@link Channel#attr}&{@link Attribute#set}。
 *
 * @description: WebSocket 握手成功事件
 * @author: wei·man cui
 * @date: 2021/4/28 10:04
 */
@Slf4j
@ChannelHandler.Sharable
public class HandshakeEventHandlerSingle extends ChannelInboundHandlerAdapter {

    public static final HandshakeEventHandlerSingle INSTANCE = new HandshakeEventHandlerSingle();

    /**
     * 监听 客户端 握手成功 的事件
     *
     * @param ctx 通道
     * @param evt 事件
     * @throws Exception 异常
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            // WebSocket 握手成功事件
            WebSocketServerProtocolHandler.HandshakeComplete event = (WebSocketServerProtocolHandler.HandshakeComplete) evt;

            final String userId = event.requestUri().split("=")[1];
            ctx.channel().attr(WsContext.USER_ID).set(userId);
            // 握手成功，将 channel 存储到 ChannelGroup 中
            WsContext.CHANNEL_GROUP.add(ctx.channel());
        } else {
            // 其他事件 交由父类 处理
            super.userEventTriggered(ctx, evt);
        }
    }
}
