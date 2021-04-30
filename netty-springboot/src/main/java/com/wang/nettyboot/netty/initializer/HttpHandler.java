package com.wang.nettyboot.netty.initializer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @author: wei·man cui
 * @date: 2021/4/30 17:23
 */
@Slf4j
public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        log.debug(msg.toString());
        final DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.content().writeBytes("hello".getBytes());
        response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, 5);

        ctx.writeAndFlush(response);
    }
}
