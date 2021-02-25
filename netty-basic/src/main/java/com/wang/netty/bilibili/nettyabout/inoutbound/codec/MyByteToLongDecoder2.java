package com.wang.netty.bilibili.nettyabout.inoutbound.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @description: 使用 ReplayingDecoder 对 之间的解码器 进行简化
 * @author: wei·man cui
 * @date: 2021/2/25 17:45
 */
public class MyByteToLongDecoder2 extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("[MyByteToEncoder2]被调用");
        list.add(byteBuf.readLong());
    }
}
