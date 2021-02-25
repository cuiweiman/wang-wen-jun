package com.wang.netty.bilibili.nettyabout.pack.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @description: 模拟 TCP 数据传输的 粘包与拆包问题、客户端 初始化
 * @author: wei·man cui
 * @date: 2021/2/25 17:53
 */
public class MyPackClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new MyClientHandler());
    }
}
