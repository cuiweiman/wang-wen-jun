package com.wang.netty.daxin.chapter02.channel;

import com.wang.netty.daxin.chapter02.eventloop.NioEventLoop;
import com.wang.netty.daxin.chapter02.handler.NioHandler;
import com.wang.netty.daxin.chapter02.handler.NioHandlerContext;

import java.nio.ByteBuffer;

/**
 * @description: 管理维护 调用链
 * @author: wei·man cui
 * @date: 2021/4/21 18:08
 */
public class NioPipeline {

    private NioChannel channel;
    private NioEventLoop eventLoop;

    public NioHandlerContext headCtx;
    public NioHandlerContext tailCtx;

    public NioPipeline(NioChannel channel, NioEventLoop eventLoop) {
        this.channel = channel;
        this.eventLoop = eventLoop;

        NioPipelineHandler handler = new NioPipelineHandler();
        this.headCtx = new NioHandlerContext(handler, channel);
        this.tailCtx = new NioHandlerContext(handler, channel);
        this.headCtx.next = tailCtx;
        this.tailCtx.prev = headCtx;
    }

    /**
     * 新增处理器 到 链中
     *
     * @param handler 处理器
     */
    public void addLast(NioHandler handler) {
        NioHandlerContext handlerContext = new NioHandlerContext(handler, channel);
        NioHandlerContext prevCtx = this.tailCtx.prev;
        // 需要先修改 尾部前一个节点的 next 指针；尾部节点的 prev指针；
        // 新增节点的 prev 指针；新增节点的 next 指针。
        prevCtx.next = handlerContext;
        this.tailCtx.prev = handlerContext;
        handlerContext.prev = prevCtx;
        handlerContext.next = this.tailCtx;
    }

    /**
     * 初始 头Handler 和 尾Handler
     */
    public class NioPipelineHandler implements NioHandler {

        @Override
        public void channelRead(NioHandlerContext ctx, Object msg) {
            System.out.println("[pipeline read 释放资源]" + msg.toString());
        }

        @Override
        public void write(NioHandlerContext ctx, Object msg) {
            System.out.println("[pipeline write]" + msg.toString());
            if (!(msg instanceof ByteBuffer)) {
                throw new RuntimeException("类型错误：" + msg.getClass());
            }
            NioPipeline.this.channel.addWriteQueue((ByteBuffer) msg);
        }

        @Override
        public void flush(NioHandlerContext ctx) {
            System.out.println("[pipeline flush]");
            NioPipeline.this.channel.doFlush();
        }
    }

}
