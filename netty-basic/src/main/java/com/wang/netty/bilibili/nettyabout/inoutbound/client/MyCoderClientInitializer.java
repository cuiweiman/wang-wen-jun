package com.wang.netty.bilibili.nettyabout.inoutbound.client;

import com.wang.netty.bilibili.nettyabout.inoutbound.codec.MyByteToLongDecoder;
import com.wang.netty.bilibili.nettyabout.inoutbound.codec.MyLongToByteEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @description: 客户端 初始化.出站时，先走业务处理器，再走编码处理器。
 * @author: wei·man cui
 * @date: 2021/2/25 17:49
 */
public class MyCoderClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        // 添加 自定义 出站 编码器
        pipeline.addLast(new MyLongToByteEncoder());

        // 添加 解码器，对服务器相应的信息进行 解码
        pipeline.addLast(new MyByteToLongDecoder());

        // 添加 自定义 Handler业务处理器。出站时，先走业务处理器，再走编码处理器。
        pipeline.addLast(new MyClientHandler());
    }
}
