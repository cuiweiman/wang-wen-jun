package com.wang.netty.bilibili.nettyabout.websocket.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Date;

/**
 * @description:
 * @author: wei·man cui
 * @date: 2021/2/25 17:14
 */
public class NettyWebsocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        System.out.println("[服务器]接收消息：" + textWebSocketFrame.text());
        // 回复浏览器
        TextWebSocketFrame responseFrame = new TextWebSocketFrame("当前服务器时间：" + new Date() + "；" + textWebSocketFrame.text());
        ctx.channel().writeAndFlush(responseFrame);
    }

    /**
     * 当 Web 客户端连接时，触发方法
     *
     * @param ctx 通道上下文
     * @throws Exception 异常
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // id() 表示唯一标识，LongText 是唯一的；ShortText 不是惟一的。
        System.out.println("handlerAdded 被调用 " + ctx.channel().id().asLongText());
        System.out.println("handlerAdded 被调用 " + ctx.channel().id().asShortText());
    }


    /**
     * 客户端 断开连接后，触发方法
     *
     * @param ctx 通道上下文
     * @throws Exception 异常
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved 被调用 " + ctx.channel().id().asLongText());
        System.out.println("handlerRemoved 被调用 " + ctx.channel().id().asShortText());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // id() 表示唯一标识，LongText 是唯一的；ShortText 不是惟一的。
        System.out.println("channelActive 被调用 " + ctx.channel().id().asLongText());
        System.out.println("channelActive 被调用 " + ctx.channel().id().asShortText());
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // id() 表示唯一标识，LongText 是唯一的；ShortText 不是惟一的。
        System.out.println("channelInactive 被调用 " + ctx.channel().id().asLongText());
        System.out.println("channelInactive 被调用 " + ctx.channel().id().asShortText());
    }

    /**
     * 发成异常
     *
     * @param ctx   通道上下文
     * @param cause 原因
     * @throws Exception 异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生 " + cause.getMessage());
        ctx.close();
    }

}
