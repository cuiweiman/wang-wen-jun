package com.wang.netty.daxin.chapter05rpc.rpcserver;

import com.wang.netty.daxin.chapter05rpc.pojo.User;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: Java对象 处理器
 * @author: wei·man cui
 * @date: 2021/4/25 14:39
 */
@Slf4j
public class RpcServerMsgHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("[服务端 RpcServerMsgHandler#channelRead]接收到数据：{}", msg.toString());

        User user = (User) msg;
        user.setAge(user.getAge() + 18);
        ctx.writeAndFlush(user);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("[服务端 RpcServerMsgHandler#exceptionCaught]发生异常： {}", cause.toString());
        ctx.close();
    }
}
