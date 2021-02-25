package com.wang.netty.bilibili.nettyabout.groupchat.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @description: 服务端
 * @author: wei·man cui
 * @date: 2021/2/24 17:42
 */
public class NettyGroupChatServer {

    private Integer port;

    public NettyGroupChatServer(Integer port) {
        this.port = port;
    }

    public void startServer() {
        EventLoopGroup bossEventLoop = new NioEventLoopGroup();
        NioEventLoopGroup workerEventLoop = new NioEventLoopGroup();
        try {
            final ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossEventLoop, workerEventLoop)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new NettyGroupChatServerInitializer());

            final ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            System.out.println("[服务器]启动成功");
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossEventLoop.shutdownGracefully();
            workerEventLoop.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new NettyGroupChatServer(8989).startServer();
    }

}
