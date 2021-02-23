package com.wang.netty.bilibili.nettyabout.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @description: netty 实现 http 协议
 * @author: wei·man cui
 * @date: 2021/2/23 16:12
 */
public class NettyHttpServer {

    public static void main(String[] args) {
        EventLoopGroup bossEventGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerEventGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossEventGroup, workerEventGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new NettyHttpServerInitializer());

            ChannelFuture channelFuture = serverBootstrap.bind(6699).sync();
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossEventGroup.shutdownGracefully();
            workerEventGroup.shutdownGracefully();
        }
    }
}
