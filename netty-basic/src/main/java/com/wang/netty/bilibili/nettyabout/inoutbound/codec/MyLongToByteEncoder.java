package com.wang.netty.bilibili.nettyabout.inoutbound.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @description: 自定义 出站 编码器
 * @author: wei·man cui
 * @date: 2021/2/25 17:47
 */
public class MyLongToByteEncoder extends MessageToByteEncoder<Long> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Long aLong, ByteBuf byteBuf) throws Exception {
        System.out.print("[MyLongToByteEncoder.encode]被调用： ");
        System.out.println("msg=" + aLong);
        // 将 信息 写出去
        byteBuf.writeLong(aLong);
    }
}
