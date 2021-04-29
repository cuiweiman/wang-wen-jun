package com.wang.netty.daxin.chapter12.boundhandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @description: Pipeline 入站和出站 规则
 * @author: wei·man cui
 * @date: 2021/4/29 11:44
 */
public class PipelineServer {

    public static void main(String[] args) {
        final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        final EventLoopGroup workGroup = new NioEventLoopGroup(1);
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("InboundHandlerA",new InHandlerA());
                            ch.pipeline().addLast("InboundHandlerB",new InHandlerB());
                            ch.pipeline().addLast("OutboundHandlerA",new OutHandlerA());
                            ch.pipeline().addLast("OutboundHandlerB",new OutHandlerB());
                        }
                    });
            ChannelFuture future = bootstrap.bind(new InetSocketAddress(9090)).sync();
            future.channel().closeFuture().addListener(channelFuture -> {
                workGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
