package com.wang.netty.bilibili.nettyabout.groupchat.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Date;

/**
 * @description:
 * @author: wei·man cui
 * @date: 2021/2/24 17:49
 */
public class NettyGroupChatServerHandler extends SimpleChannelInboundHandler<String> {


    /**
     * 保存客户端 通道；可以同时向 组内的所有通道传送数据
     */
    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 客户端建立连接成功，将客户端通道记录到 ChannelGroup 中
     * <p>ChannelHandler的生命周期：
     * ChannelHandler 添加到 ChannelPipeline 时触发
     *
     * @param ctx 通道上下文
     * @throws Exception 异常
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        final Channel channel = ctx.channel();
        channels.writeAndFlush("[服务器]客户加入聊天室：" + channel.remoteAddress());
        channels.add(channel);
    }

    /**
     * 客户端断开连接，触发本方法。
     * <p>ChannelHandler的生命周期：
     * ChannelHandler 从 ChannelPipeline 移除时触发
     *
     * @param ctx 通道上下文
     * @throws Exception 异常
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        final Channel channel = ctx.channel();
        channels.writeAndFlush("[服务器]客户离开聊天室：" + channel.remoteAddress()
                + "，剩余在线用户：" + channels.size());
    }

    /**
     * 获得客户端连接，客户端上线
     * <p> Channel 生命周期
     * Channel 处于活动状态，已连接到远程节点，可以收发消息时，触发
     *
     * @param ctx 通道上下文
     * @throws Exception 异常
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[服务器] 客户" + ctx.channel().remoteAddress() + " 上线，时间：" + new Date());
    }


    /**
     * 失去客户端连接，客户端离线
     * <p> Channel 生命周期
     * Channel 失去远程节点的连接时 触发
     *
     * @param ctx 通道上下文
     * @throws Exception 异常
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[服务器] 客户" + ctx.channel().remoteAddress() + " 离线，时间：" + new Date());
    }

    /**
     * 通道读取数据事件
     *
     * @param ctx 通道上下文
     * @param msg 信息
     * @throws Exception 异常
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        final Channel channel = ctx.channel();
        channels.forEach(otherChannel -> {
            if (channel != otherChannel) {
                otherChannel.writeAndFlush("信息 From [" + channel.remoteAddress() + "]：" + msg);
            } else {
                otherChannel.writeAndFlush("[本人发送信息]：" + msg);
            }
        });
    }

    /**
     * 通道读取完成
     *
     * @param ctx 通道上下文
     * @throws Exception 异常
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[服务器]来自 " + ctx.channel().remoteAddress() + " 的信息读取完成");
    }

    /**
     * 发生异常，关闭上下文通道
     * <p>ChannelHandler的生命周期：
     * 处理过程中，在 ChannelPipeline 中发生错误是 触发
     *
     * @param ctx   通道上下文
     * @param cause 原因
     * @throws Exception 异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
