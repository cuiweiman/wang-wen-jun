package com.wang.netty.daxin.chapter02.handler;

/**
 * @description: 处理器
 * @author: wei·man cui
 * @date: 2021/4/21 18:07
 */
public interface NioHandler {


    /**
     * 读事件
     *
     * @param ctx ctx
     * @param msg msg
     */
    void channelRead(NioHandlerContext ctx, Object msg);

    /**
     * 写事件，msg写入缓冲区
     *
     * @param ctx ctx
     * @param msg msg
     */
    void write(NioHandlerContext ctx, Object msg);

    /**
     * 刷新缓冲区，将消息写出
     *
     * @param ctx ctx
     */
    void flush(NioHandlerContext ctx);
}
