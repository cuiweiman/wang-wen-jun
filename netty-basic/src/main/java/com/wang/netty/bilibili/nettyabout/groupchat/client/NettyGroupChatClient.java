package com.wang.netty.bilibili.nettyabout.groupchat.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;

/**
 * @description: 客户端
 * @author: wei·man cui
 * @date: 2021/2/24 17:43
 */
public class NettyGroupChatClient {
    private String host;

    private Integer port;

    public NettyGroupChatClient(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public void startClient() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new NettyGroupChatClientInitializer());
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            Channel channel = channelFuture.channel();
            System.out.println("[客户端]" + channel.remoteAddress());
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String msg = scanner.nextLine();
                channel.writeAndFlush(msg);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new NettyGroupChatClient("localhost", 8989)
                .startClient();
    }
}
