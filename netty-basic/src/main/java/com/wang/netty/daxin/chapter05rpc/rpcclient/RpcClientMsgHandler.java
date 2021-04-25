package com.wang.netty.daxin.chapter05rpc.rpcclient;

import com.wang.netty.daxin.chapter05rpc.pojo.User;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @description: Java对象 处理器
 * @author: wei·man cui
 * @date: 2021/4/25 15:26
 */
@Slf4j
public class RpcClientMsgHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("[客户端 RpcClientMsgHandler#channelRead]接收到数据" + msg.toString());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        User user = new User();
        user.setName("Jack");
        user.setAge(18);
        user.setBirthDay(new Date());
        ctx.writeAndFlush(user);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("[客户端 RpcServerMsgHandler#exceptionCaught]发生异常： {}", cause.toString());
        ctx.close();
    }
}
