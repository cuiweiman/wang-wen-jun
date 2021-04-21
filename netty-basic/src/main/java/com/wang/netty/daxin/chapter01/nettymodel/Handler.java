package com.wang.netty.daxin.chapter01.nettymodel;

/**
 * 模拟 Netty 的 Pipeline 机制
 *
 * @description: 优化 MyChannel 的事件处理
 * @author: wei·man cui
 * @date: 2021/4/21 15:09
 */
public interface Handler {

    /**
     * 读事件
     *
     * @param ctx ctx
     * @param msg msg
     */
    void channelRead(HandlerContext ctx, Object msg);

    /**
     * 写事件
     *
     * @param ctx ctx
     * @param msg msg
     */
    void write(HandlerContext ctx, Object msg);

    /**
     * @param ctx ctx
     */
    void flush(HandlerContext ctx);
}
