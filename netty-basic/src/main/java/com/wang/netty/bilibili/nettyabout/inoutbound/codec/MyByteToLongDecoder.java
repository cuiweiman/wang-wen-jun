package com.wang.netty.bilibili.nettyabout.inoutbound.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @description: Byte to Long 解码器。入站时，先走解码器，再走业务处理器
 * @author: wei·man cui
 * @date: 2021/2/25 17:50
 */
public class MyByteToLongDecoder extends ByteToMessageDecoder {


    /**
     * 自定义 解码器
     *
     * @param channelHandlerContext 上下文
     * @param byteBuf               入站的缓冲区
     * @param list                  数据集合，解码后传递给下一个 handler 进行处理
     * @throws Exception 异常
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("[MyByteToLongDecoder.decode] 解码器 被调用");
        // Long 的长度为 8个字节，只有达到8个字节才允许读取
        if (internalBuffer().readableBytes() >= 8) {
            list.add(internalBuffer().readLong());
        }
    }


}
