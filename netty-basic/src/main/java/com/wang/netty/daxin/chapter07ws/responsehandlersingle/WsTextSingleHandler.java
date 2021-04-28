package com.wang.netty.daxin.chapter07ws.responsehandlersingle;

import com.wang.netty.daxin.chapter07ws.responsehandler.WsContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelMatcher;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * 在 客户端 Chanel 中保存附加的 用户ID；
 * 点对点 发送消息时，寻找 接收者 匹配的 Channel，进行消息发送。
 * {@link io.netty.channel.group.ChannelGroup#writeAndFlush(Object, ChannelMatcher)}
 *
 * @description: 点对点 消息单发
 * @author: wei·man cui
 * @date: 2021/4/28 14:50
 */
@Slf4j
public class WsTextSingleHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        final String data = msg.text();
        // 客户端 发送消息的格式为  userId=The msg to be sent;
        final String toUserId = data.split("=")[0];
        log.info("[WsTextSingleHandler#channelRead0]接收到消息：{}", data);
        WsContext.CHANNEL_GROUP.writeAndFlush(new TextWebSocketFrame(data), new ChannelMatcher() {
            @Override
            public boolean matches(Channel channel) {
                return Objects.equals(ctx.channel().attr(WsContext.USER_ID).get(), toUserId);
            }
        });
    }

}
