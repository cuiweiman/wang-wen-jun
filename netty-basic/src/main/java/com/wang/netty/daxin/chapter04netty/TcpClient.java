package com.wang.netty.daxin.chapter04netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @description: 模拟TCP拆包
 * @date: 2021/4/24 9:40
 * @author: wei·man cui
 */
public class TcpClient {

    public static void main(String[] args) {
        EventLoopGroup loopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(loopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast("StringDecoder", new StringDecoder());
                            socketChannel.pipeline().addLast("StringEncoder", new StringEncoder());
                            socketChannel.pipeline().addLast("client", new TcpClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect("127.0.0.1", 9090).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            loopGroup.shutdownGracefully();
        }
    }


}
