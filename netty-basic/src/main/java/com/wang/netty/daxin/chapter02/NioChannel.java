package com.wang.netty.daxin.chapter02;

import com.wang.netty.daxin.chapter01.EventLoop2;
import com.wang.netty.daxin.chapter01.MyChannel;
import com.wang.netty.daxin.chapter01.nettymodel.HandlerContext;
import com.wang.netty.daxin.chapter01.nettymodel.MyHandler1;
import com.wang.netty.daxin.chapter01.nettymodel.MyHandler2;
import com.wang.netty.daxin.chapter01.nettymodel.Pipeline;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 负责 处理 IO的 读写 事件，将 NioEventLoop 的读写事件进行解耦，NioEventLoop 只进行事件分发 和 监听事件的注册
 * <p>
 * 读数据 事件：NioEventLoop 监听到 可读事件后，传送给 NioChannel。
 * {@link NioChannel#read} 将数据 经过方法 {@link HandlerContext#fireChannelRead} 传递给
 * 链式调用的处理器 {@link MyHandler1#channelRead}进行链式处理调用，在链式处理过程中，通过
 * {@link HandlerContext#fireChannelRead} 方法 进行 消息的链式传递。{@link Pipeline} 中维护了链式处理器的顺序。
 *
 * <p>
 * 写数据 事件： NioEventLoop 监听到 可写事件后，传送给 NioChannel。
 * 1. 调用 {@link NioChannel#doWrite}触发写事件，经由方法 {@link HandlerContext#write} 传递给 链式调用的
 * 处理器 {@link MyHandler2#write}进行链式处理调用，并经过 {@link HandlerContext#write} 方法有 tail 节点向 head 节点
 * 传递写数据。
 * 2. 写数据最终到达 {@link Pipeline.PipelineHandler#write} 方法中，将 写数据 添加到 ByteBuffer 缓冲队列中。
 * 3. {@link MyHandler2#flush} 方法 依次从 tail 节点 传递到 head 节点，最终到达 {@link Pipeline.PipelineHandler#flush} 方法，
 * 在该方法中 触发了 {@link MyChannel#doFlush()} 方法，将 当前的 selector 监听事件 改成了 OP_WRITE 可写事件。
 * 4. {@link EventLoop2} 监听到 可写事件 后，将消息分发给 {@link MyChannel#write} 方法，最终将消息写入到 Channel 中。
 *
 * @description: 处理 NioEventLoop 分发过来的 读写事件
 * @author: wei·man cui
 * @date: 2021/4/21 18:06
 */
public class NioChannel {

    private SocketChannel channel;

    private NioEventLoop eventLoop;


    /**
     * 写消息队列，需要向 channel 中写入消息时，放入队列即可
     */
    private Queue<ByteBuffer> writeQueue = new ArrayBlockingQueue<>(16);

    private NioPipeline pipeline;


    public NioChannel(SocketChannel channel, NioEventLoop eventLoop) {
        this.channel = channel;
        this.eventLoop = eventLoop;

        // this.pipeline = new NioPipeline(this, eventLoop);
        // this.pipeline.addLast(new MyHandler1());
        // this.pipeline.addLast(new MyHandler2());
    }

    public void doWrite(Object msg) {
        // this.pipeline.tailCtx.write(msg);
    }

}
