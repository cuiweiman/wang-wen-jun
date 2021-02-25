package com.wang.netty.bilibili.nettyabout.pack.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @description: 模拟 TCP 数据传输的 粘包与拆包问题，Netty 客户端
 * @author: wei·man cui
 * @date: 2021/2/25 17:54
 */
public class MyPackClient {
    public static void main(String[] args) throws Exception {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new MyPackClientInitializer());

            ChannelFuture future = bootstrap.connect("127.0.0.1", 9999).sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }


}
