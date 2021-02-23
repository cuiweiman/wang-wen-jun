package com.wang.netty.bilibili.nettyabout.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * @description: netty 实现简单的 http：handler
 * @author: wei·man cui
 * @date: 2021/2/23 16:13
 */
public class NettyHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * 触发读取事件
     *
     * @param channelHandlerContext 通道上下文
     * @param httpObject            http数据
     * @throws Exception 异常
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        if (httpObject instanceof HttpRequest) {
            System.out.println("[服务端] 数据类型： " + httpObject.getClass());
            System.out.println("[服务端] 客户端地址： " + channelHandlerContext.channel().remoteAddress());

            HttpRequest httpRequest = (HttpRequest) httpObject;
            final URI uri = new URI(httpRequest.uri());
            if ("/favico.ico".equals(uri.getPath())) {
                System.out.println("[服务端]请求了 favico.ico，不做处理 ");
                return;
            }

            ByteBuf byteBuf = Unpooled.copiedBuffer("[一将功成万骨枯]", CharsetUtil.UTF_8);
            FullHttpResponse response =
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=utf-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());

            channelHandlerContext.writeAndFlush(response);
        }
    }
}
