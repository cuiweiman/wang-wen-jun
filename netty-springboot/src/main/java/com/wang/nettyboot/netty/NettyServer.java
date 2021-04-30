package com.wang.nettyboot.netty;

import com.wang.nettyboot.config.NettyConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: Netty 服务器
 * @author: wei·man cui
 * @date: 2021/4/30 9:56
 */
@Slf4j
@Component
public class NettyServer {

    @Resource
    private NettyConfig nettyConfig;

    AtomicInteger countClientCount = new AtomicInteger(0);

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup(128);
        try {
            final ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    // bossGroup 重用缓冲区
                    .option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT)
                    // bossGroup 临时存储 三次握手后的连接 队列长度
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // workGroup 接收缓冲区大小
                    .childOption(NioChannelOption.SO_RCVBUF, 8 * 1024)
                    // workGroup 发送缓冲区大小
                    .childOption(NioChannelOption.SO_SNDBUF, 8 * 1024)
                    // workGroup 保持长连接
                    .childOption(NioChannelOption.SO_KEEPALIVE, true)
                    // workGroup 重用缓冲区
                    .childOption(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT)
                    .childHandler(new NettyServerInitializer(countClientCount));

            int port = nettyConfig.getServerPort();
            serverBootstrap.bind(port).sync().channel().closeFuture().addListener(channelFuture -> {
                log.info("Netty Server stop in gracefully...");
                bossGroup.shutdownGracefully();
                workGroup.shutdownGracefully();
            });
            log.info("Netty Server start on port: {}", port);

            /*for (int i = 0; i < 50; i++) {
                int port = nettyConfig.getServerPort() + i;
                serverBootstrap.bind(port).sync().channel().closeFuture().addListener(channelFuture -> {
                    log.info("Netty Server stop in gracefully...");
                    bossGroup.shutdownGracefully();
                    workGroup.shutdownGracefully();
                });
                log.info("Netty Server start on port: {}", port);
            }*/

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
