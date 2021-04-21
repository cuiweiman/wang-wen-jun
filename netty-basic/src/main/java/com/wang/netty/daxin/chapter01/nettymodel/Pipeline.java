package com.wang.netty.daxin.chapter01.nettymodel;


import com.wang.netty.daxin.chapter01.EventLoop2;
import com.wang.netty.daxin.chapter01.MyChannel;

import java.nio.ByteBuffer;

/**
 * @description: 链式调用
 * @author: wei·man cui
 * @date: 2021/4/21 15:41
 */
public class Pipeline {

    private MyChannel myChannel;

    private EventLoop2 eventLoop;

    /**
     * 头部 handler
     */
    public HandlerContext headCtx;
    /**
     * 尾部 handler
     */
    public HandlerContext tailCtx;

    public Pipeline(MyChannel myChannel, EventLoop2 eventLoop) {
        this.myChannel = myChannel;
        this.eventLoop = eventLoop;

        // 搭建 链式关系
        PipelineHandler handler = new PipelineHandler();
        this.headCtx = new HandlerContext(handler, myChannel);
        this.tailCtx = new HandlerContext(handler, myChannel);
        this.headCtx.next = tailCtx;
        this.tailCtx.prev = headCtx;
    }

    /**
     * 新增处理器 到 链中
     *
     * @param handler 处理器
     */
    public void addLast(Handler handler) {
        final HandlerContext handlerContext = new HandlerContext(handler, myChannel);
        // 需要先修改 尾部前一个节点的 next 指针；尾部节点的 prev指针；
        // 新增节点的 prev 指针；新增节点的 next 指针。
        HandlerContext prevCtx = this.tailCtx.prev;
        prevCtx.next = handlerContext;
        this.tailCtx.prev = handlerContext;
        handlerContext.prev = prevCtx;
        handlerContext.next = this.tailCtx;
    }


    /**
     * 初始 头Handler 和 尾Handler
     */
    public class PipelineHandler implements Handler {

        @Override
        public void channelRead(HandlerContext ctx, Object msg) {
            System.out.println("[pipeline read 释放资源]" + msg.toString());
        }

        @Override
        public void write(HandlerContext ctx, Object msg) {
            System.out.println("[pipeline write]" + msg.toString());
            if (!(msg instanceof ByteBuffer)) {
                throw new RuntimeException("类型错误：" + msg.getClass());
            }
            Pipeline.this.myChannel.addWriteQueue((ByteBuffer) msg);
        }

        @Override
        public void flush(HandlerContext ctx) {
            System.out.println("[pipeline flush]");
            Pipeline.this.myChannel.doFlush();
        }
    }

}
