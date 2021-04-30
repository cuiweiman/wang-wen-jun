package com.wang.nettyboot.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @description: 测试 netty 最大连接数
 * @author: wei·man cui
 * @date: 2021/4/30 11:06
 */
@Slf4j
public class NettyClientTest implements Runnable {

    public void clientConnect() {
        final EventLoopGroup loopGroup = new NioEventLoopGroup();
        try {
            final Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(loopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new StringEncoder());
                            ch.pipeline().addLast(new SendMsgHandler());
                        }
                    });

            for (int i = 0; i < 13600; i++) {
                // bootstrap.connect(new InetSocketAddress("172.16.30.172", 9100)).sync().channel().closeFuture().addListener(channelFuture -> {
                bootstrap.connect(new InetSocketAddress("127.0.0.1", 9100)).sync().channel().closeFuture().addListener(channelFuture -> {
                    if (!channelFuture.isSuccess()) {
                        log.error("Client connect failed on port {}", 9100);
                    }
                });
            }

            /*for (int i = 0; i < 13600; i++) {
                final int port = 9100 + i % 50;
                bootstrap.connect(new InetSocketAddress(port)).sync().channel().closeFuture().addListener(channelFuture -> {
                    if (!channelFuture.isSuccess()) {
                        log.error("Client connect failed on port {}", port);
                    }
                });
            }*/

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void run() {
        this.clientConnect();
    }

    public static void main(String[] args) {
        new Thread(new NettyClientTest()).start();
    }


}
