package com.wang.netty.bilibili.nettyabout.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @description: netty实现tcp服务
 * @author: wei·man cui
 * @date: 2021/2/23 15:22
 */
public class NettyTcpServer {
    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    // 使用 NioServerSocketChannel 作为服务器的通道实现
                    .channel(NioServerSocketChannel.class)
                    // 设置 线程队列 等待 连接的 个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 设置保持 活动/生存的 连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyTcpServerHandler());
                        }
                    });
            System.out.println("[服务器]启动成功");
            ChannelFuture channelFuture = serverBootstrap.bind(6688).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
