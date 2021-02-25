package com.wang.netty.bilibili.nettyabout.inoutbound.server;

import com.wang.netty.bilibili.nettyabout.inoutbound.codec.MyByteToLongDecoder2;
import com.wang.netty.bilibili.nettyabout.inoutbound.codec.MyLongToByteEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @description:
 * @author: wei·man cui
 * @date: 2021/2/25 17:45
 */
public class MyCoderServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        // 添加自定义解码器，对入站 客户端信息 进行解码
        // pipeline.addLast(new MyByteToLongDecoder());

        pipeline.addLast(new MyByteToLongDecoder2());

        // 添加 自定义 编码器， 对出站的 服务器消息 进行编码
        pipeline.addLast(new MyLongToByteEncoder());

        pipeline.addLast(new MyCoderServerHandler());

    }
}
