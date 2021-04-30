package com.wang.nettyboot.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: netty 服务器配置
 * @author: wei·man cui
 * @date: 2021/4/30 9:57
 */
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

    private AtomicInteger countClientCount;

    public NettyServerInitializer(AtomicInteger countClientCount) {
        this.countClientCount = countClientCount;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
        ch.pipeline().addLast("StringEncoder", new StringEncoder());
        ch.pipeline().addLast("StringDecoder", new StringDecoder());
        ch.pipeline().addLast("ReceiveMsgHandler", new ReceiveMsgHandler(countClientCount));
    }
}
