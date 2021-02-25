package com.wang.netty.bilibili.nettyabout.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @description: 心跳检测
 * @author: wei·man cui
 * @date: 2021/2/25 14:15
 */
public class MyHeartbeatServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当IdleStateEvent触发后，事件会传递给 管道的 下一个handler 的 userEventTriggered 去处理。
     * 根据读、写、读写 空闲，分别做处理
     *
     * @param ctx 通道上下文
     * @param evt 心跳检测 触发的事件
     * @throws Exception 异常
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventType = null;
            switch (event.state()) {
                case READER_IDLE:
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    break;
                case ALL_IDLE:
                    eventType = "读写空闲";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress() + " 超时事件： " + eventType);
            ctx.channel().close();
            System.out.println("发生空闲，直接关闭通道");
        }
    }
}
