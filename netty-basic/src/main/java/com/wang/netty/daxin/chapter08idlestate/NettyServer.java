package com.wang.netty.daxin.chapter08idlestate;

import com.wang.netty.daxin.chapter08idlestate.handler.HandshakeEventListenHandler;
import com.wang.netty.daxin.chapter08idlestate.handler.IdleStateEventHandler;
import com.wang.netty.daxin.chapter08idlestate.handler.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @description: 心跳机制
 * @author: wei·man cui
 * @date: 2021/4/28 15:23
 */
@Slf4j
public class NettyServer {

    public static void main(String[] args) {
        final NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        final NioEventLoopGroup workGroup = new NioEventLoopGroup(16);
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("握手事件监听器", new HandshakeEventListenHandler());
                            /*
                                readerIdleTimeSeconds：读超时,触发 IdleStateEvent#READER_IDLE 事件
                                writerIdleTimeSeconds：写超时,触发 IdleStateEvent#WRITER_IDLE 事件
                                allIdleTimeSeconds：读/写超时,触发 IdleStateEvent#ALL_IDLE 事件
                             */
                            ch.pipeline().addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS));
                            ch.pipeline().addLast("心跳事件监听处理器", new IdleStateEventHandler());
                            ch.pipeline().addLast(new StringEncoder());
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });
            final ChannelFuture future = serverBootstrap.bind(9090).sync();
            future.channel().closeFuture().addListener(channelFuture -> {
                log.info("[NettyServer#main] 服务端关闭连接");
                workGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
