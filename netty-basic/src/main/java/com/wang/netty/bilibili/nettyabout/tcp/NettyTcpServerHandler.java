package com.wang.netty.bilibili.nettyabout.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: wei·man cui
 * @date: 2021/2/23 15:31
 */
public class NettyTcpServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 客户端发起连接，服务端通道就绪，触发本方法
     *
     * @param ctx 通道
     * @throws Exception 异常
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[服务端]通道连接准备就绪");
    }

    /**
     * 客户端连接后发送数据，服务端接收数据时触发
     *
     * @param ctx 通道
     * @param msg 数据
     * @throws Exception 异常
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 用户 自定义 普通任务
        ctx.channel().eventLoop().execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                ctx.writeAndFlush(Unpooled.copiedBuffer("[服务端]客户端处理耗时阻塞", CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // 自定义 定时任务 提交到 ScheduledTaskQueue队列中。
        ctx.channel().eventLoop().schedule(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                ctx.writeAndFlush(Unpooled.copiedBuffer("[服务端]客户端定时任务", CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 5, TimeUnit.SECONDS);

        // 读取 客户端上传的 信息
        System.out.println("[服务端]服务端读取线程：" + Thread.currentThread().getName());
        ByteBuf byteBuffer = (ByteBuf) msg;
        String message = byteBuffer.toString(CharsetUtil.UTF_8);
        System.out.println("[服务端]服务端接收数据：" + message);
    }

    /**
     * 服务端数据读取结束后触发
     *
     * @param ctx 通道
     * @throws Exception 异常
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("[服务端]数据接收完毕", CharsetUtil.UTF_8));
    }

    /**
     * 发生异常，关闭通道
     *
     * @param ctx   通道
     * @param cause 原因
     * @throws Exception 异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
