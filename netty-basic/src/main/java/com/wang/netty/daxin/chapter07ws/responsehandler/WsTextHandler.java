package com.wang.netty.daxin.chapter07ws.responsehandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * ChannelGroup 群发
 *
 * @description: 接收 客户端消息，向其他客户端全体进行 消息转发
 * @author: wei·man cui
 * @date: 2021/4/28 11:30
 */
@Slf4j
public class WsTextHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String data = msg.text();
        log.info("[WsTextHandler#channelRead]读到消息：{}", data);
        WsContext.CHANNEL_GROUP.writeAndFlush(new TextWebSocketFrame(data));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("[WsTextHandler#exceptionCaught] 异常：{}", cause.toString());
    }
}
