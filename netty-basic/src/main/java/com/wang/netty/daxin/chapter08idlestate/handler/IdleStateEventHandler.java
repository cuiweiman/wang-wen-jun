package com.wang.netty.daxin.chapter08idlestate.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 心跳事件 触发机制
 * @author: wei·man cui
 * @date: 2021/4/28 15:39
 */
@Slf4j
public class IdleStateEventHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            // 如果 发生了 心跳事件，那么进行处理
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventType = null;
            switch (event.state()) {
                case READER_IDLE:
                    eventType = ctx.channel().remoteAddress().toString().concat(" 读超时");
                    break;
                case WRITER_IDLE:
                    eventType = ctx.channel().remoteAddress().toString().concat(" 写超时");
                    break;
                case ALL_IDLE:
                    eventType = ctx.channel().remoteAddress().toString().concat(" 读/写超时");
                    break;
                default:
                    eventType = ctx.channel().remoteAddress().toString().concat(" 还能有其他事件么？");
                    break;
            }
            log.info("[IdleStateEventHandler#userEventTriggered] 心跳监控事件： {}", eventType);
        } else {
            ctx.fireChannelRead(evt);
        }
    }
}
