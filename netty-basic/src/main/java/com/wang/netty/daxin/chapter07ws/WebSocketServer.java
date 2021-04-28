package com.wang.netty.daxin.chapter07ws;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolConfig;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: Netty 实现 WebSocket 服务端
 * @author: wei·man cui
 * @date: 2021/4/26 15:56
 */
@Slf4j
public class WebSocketServer {

    public void httpServer(int port) {
        final EventLoopGroup boosGroup = new NioEventLoopGroup(1);
        final EventLoopGroup workGroup = new NioEventLoopGroup(16);
        ServerBootstrap bootstrap;
        try {
            bootstrap = new ServerBootstrap();
            bootstrap.group(boosGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 处理 Http 请求的编解码器——请求 解码器
                            ch.pipeline().addLast("HttpRequestDecoder", new HttpRequestDecoder());
                            // 处理 Http 请求的编解码器——响应 编码器
                            ch.pipeline().addLast("HttpResponseEncoder", new HttpResponseEncoder());
                            // 当一个 Http 请求比较大时，解码器对报文进行分块解析，解析一部分存储在 aggregator聚合器中，
                            // 直至报文解析结束，聚合器继续向下传递数据。
                            ch.pipeline().addLast("aggregator", new HttpObjectAggregator(655360));

                            /*
                            配置 WebSocket 的编解码器
                             */
                            final WebSocketServerProtocolConfig wsConfig = WebSocketServerProtocolConfig.newBuilder()
                                    .websocketPath("/websocket")
                                    .maxFramePayloadLength(Integer.MAX_VALUE)
                                    .checkStartsWith(true).build();
                            ch.pipeline().addLast("WebSocketHandler", new WebSocketServerProtocolHandler(wsConfig));
                            ch.pipeline().addLast("WebSocketTextHandler", new WebSocketTextHandler());
                            ch.pipeline().addLast("HandshakeEventHandler", HandshakeEventHandler.INSTANCE);
                        }
                    });
            final ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().addListener(channelFuture -> {
                log.debug("[WebSocket 服务端] 关闭服务器");
                boosGroup.shutdownGracefully();
                workGroup.shutdownGracefully();
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        WebSocketServer httpServer = new WebSocketServer();
        httpServer.httpServer(9090);
    }

}
