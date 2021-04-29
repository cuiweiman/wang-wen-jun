package com.wang.netty.daxin.chapter12.boundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @description: 入站 2
 * @author: wei·man cui
 * @date: 2021/4/29 13:54
 */
@Slf4j
public class InHandlerB extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        try {
            log.debug("[InHandlerB#channelRead] {}", msg.toString());
            final ChannelFuture future = ctx.channel().write(Unpooled.wrappedBuffer("abc".getBytes()));
            future.addListener((channelFuture) -> {
                if (channelFuture.isSuccess()) {
                    log.info("写入成功");
                } else {
                    log.info("写入失败");
                }
            });
            String rec = new String(ByteBufUtil.getBytes(buf));
            if (Objects.equals(rec, "flush")) {
                ctx.channel().flush();
            }
        } finally {
            buf.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("错误");
        cause.printStackTrace();
    }
}
