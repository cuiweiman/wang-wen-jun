package com.wang.netty.daxin.chapter05rpc.rpcserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: RPC 协议实现
 * @author: wei·man cui
 * @date: 2021/4/25 11:10
 */
@Slf4j
public class RpcServer {

    public static void main(String[] args) {
        final EventLoopGroup bossLoopGroup = new NioEventLoopGroup(1);
        final EventLoopGroup workerLoopGroup = new NioEventLoopGroup(16);
        ServerBootstrap serverBootstrap = null;
        try {
            serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossLoopGroup, workerLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("ObjectDecoder", new ObjectDecoder(Integer.MAX_VALUE,
                                    ClassResolvers.weakCachingResolver(null)));
                            ch.pipeline().addLast("ObjectEncoder", new ObjectEncoder());
                            ch.pipeline().addLast("RpcServerMsgHandler", new RpcServerMsgHandler());
                        }
                    });

            // 绑定端口
            final ChannelFuture future = serverBootstrap.bind(9090).sync();

            // 阻塞当前线程，直到 Server端关闭服务
            future.channel().closeFuture().addListener(channelFuture -> {
                log.debug("[RPC 服务端] 关闭RPC服务器");
                workerLoopGroup.shutdownGracefully();
                bossLoopGroup.shutdownGracefully();
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
