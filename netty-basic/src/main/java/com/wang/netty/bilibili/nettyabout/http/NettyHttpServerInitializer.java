package com.wang.netty.bilibili.nettyabout.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @description: netty 实现简单的 http 协议：配置 解码器、handler
 * @author: wei·man cui
 * @date: 2021/2/23 16:12
 */
public class NettyHttpServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 管道中添加 netty 提供的 http 编解码器
        pipeline.addLast("MyNettyHttpServerCodec", new HttpServerCodec());
        pipeline.addLast("MyNettyHttpServerHandler", new NettyHttpServerHandler());
    }
}
