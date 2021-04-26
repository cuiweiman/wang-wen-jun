package com.wang.netty.daxin.chapter06http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: Http 报文处理器类
 * @author: wei·man cui
 * @date: 2021/4/26 16:24
 */
@Slf4j
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpRequest> {

    private static final byte[] CONTENT = {'H', 'e', 'l', 'l', 'o', 'S', 'y', 'y'};

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) throws Exception {
        FullHttpRequest req = (FullHttpRequest) msg;
        final boolean keepAlive = HttpUtil.isKeepAlive(req);
        final FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(req.protocolVersion(), HttpResponseStatus.OK,
                Unpooled.wrappedBuffer(CONTENT));
        fullHttpResponse.headers()
                .set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN)
                .setInt(HttpHeaderNames.CONTENT_LENGTH, fullHttpResponse.content().readableBytes());
        if (keepAlive) {
            if (!req.protocolVersion().isKeepAliveDefault()) {
                fullHttpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }
        } else {
            fullHttpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        }

        final ChannelFuture write = ctx.write(fullHttpResponse);
        if (!keepAlive) {
            write.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // channelRead0 读取结束后，触发本函数
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("[Http服务端]错误：{}", cause.toString());
        ctx.close();
    }
}
