package com.wang.nettyboot.netty;

import com.google.common.eventbus.AsyncEventBus;
import com.wang.nettyboot.config.AsyncConfig;
import com.wang.nettyboot.config.EventSubscribe;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: 简单的接收数据信息
 * @author: wei·man cui
 * @date: 2021/4/30 10:07
 */
@Slf4j
public class ReceiveMsgHandler extends SimpleChannelInboundHandler<String> {

    private AtomicInteger countClientCount;

    public ReceiveMsgHandler(AtomicInteger countClientCount) {
        this.countClientCount = countClientCount;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info("[ReceiveMsgHandler#channelRead0] 接收到信息：{}", msg);
        AsyncEventBus eventBus = new AsyncEventBus("EventBusName", new AsyncConfig().executorService());
        eventBus.register(new EventSubscribe());
        eventBus.post(msg);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("[ReceiveMsgHandler#channelRegistered] 连接成功，当前连接数为：{}", this.countClientCount.getAndIncrement());
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("[ReceiveMsgHandler#channelUnregistered] 连接断开，当前连接数为：{}", this.countClientCount.getAndDecrement());
        super.channelUnregistered(ctx);
    }
}
