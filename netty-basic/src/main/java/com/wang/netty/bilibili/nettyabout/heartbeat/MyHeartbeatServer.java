package com.wang.netty.bilibili.nettyabout.heartbeat;

import com.wang.netty.bilibili.nettyabout.groupchat.client.NettyGroupChatClient;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @description: 心跳检测 机制 服务端，客户端可以直接使用 {@link NettyGroupChatClient}
 * @author: wei·man cui
 * @date: 2021/2/25 14:14
 */
public class MyHeartbeatServer {
    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // 添加一个 Netty的 日志处理器
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            /*
                             * 添加 Netty提供的 IdleStateHandler 处理空闲状态的处理器。
                             * 参数说明：
                             *      readerIdleTime：表示多长时间没有读，就会发出一个心跳检测包，检测客户端是否仍处于连接。
                             *      writerIdleTime：表示多长时间没有写，就会发出一个心跳检测包，检测客户端是否仍处于连接。
                             *      allIdleTime：表示多长时间没有读和写，就会发出一个心跳检测包，检测客户端是否仍处于连接。
                             * 当 IdleStateEvent 触发后，事件会传递给 管道的 下一个handler 的 userEventTriggered 去处理。
                             * 因此，IdleStateHandler 的下一个 handler 必须包含 userEventTriggered() 函数，根据 IdleStateEvent 做对应处理。
                             */
                            pipeline.addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));
                            pipeline.addLast(new MyHeartbeatServerHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(8989).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
