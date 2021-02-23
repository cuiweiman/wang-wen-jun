package com.wang.netty.bilibili.nettyabout.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @description: netty 实现 tcp服务
 * @author: wei·man cui
 * @date: 2021/2/23 15:22
 */
public class NettyTcpClient {
    public static void main(String[] args) {
        final NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    // 设置客户端 通道 是 NioSocketChannel 类型
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyTcpClientHandler());
                        }
                    });
            System.out.println("[客户端]启动成功");
            ChannelFuture channelFuture = bootstrap.connect("localhost", 6688);
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }

    }

}
