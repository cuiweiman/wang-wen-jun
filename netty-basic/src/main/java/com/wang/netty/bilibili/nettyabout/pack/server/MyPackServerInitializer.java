package com.wang.netty.bilibili.nettyabout.pack.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @description: 模拟 TCP 数据传输的 粘包与拆包问题
 * @author: wei·man cui
 * @date: 2021/2/25 17:52
 */
public class MyPackServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new MyPackServerHandler());
    }
}
