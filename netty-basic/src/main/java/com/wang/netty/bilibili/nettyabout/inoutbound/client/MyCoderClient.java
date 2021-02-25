package com.wang.netty.bilibili.nettyabout.inoutbound.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @description: 通过 实现自定义编解码器，理解 Netty 的 Handler 链的调用机制。
 * @author: wei·man cui
 * @date: 2021/2/25 17:49
 */
public class MyCoderClient {
    public static void main(String[] args) throws Exception {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new MyCoderClientInitializer());

            ChannelFuture future = bootstrap.connect("127.0.0.1", 9999).sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }


}
