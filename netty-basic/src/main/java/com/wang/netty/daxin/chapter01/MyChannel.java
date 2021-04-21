package com.wang.netty.daxin.chapter01;

import com.wang.netty.daxin.chapter01.nettymodel.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 负责 处理 IO的读写 事件，事件由 EventLoop2 分发过来
 * <p>
 * 封装了 SocketChannel 和 EventLoop2 事件监听和注册 这两个对象，
 * 可以在 MyChannel 中专门进行 事件注册和分发，从而和 EventLoop 解耦。
 *
 * <p>
 * 最终优化：{@link Pipeline} {@link Handler} {@link HandlerContext}
 *
 * <p>
 * 读数据 事件：EventLoop2 监听到 可读事件后，传送给 MyChannel。
 * {@link MyChannel#read} 将数据 经过方法 {@link HandlerContext#fireChannelRead} 传递给
 * 链式调用的处理器 {@link MyHandler1#channelRead}进行链式处理调用，在链式处理过程中，通过
 * {@link HandlerContext#fireChannelRead} 方法 进行 消息的链式传递。{@link Pipeline} 中维护了链式处理器的顺序。
 *
 * <p>
 * 写数据 事件： EventLoop2 监听到 可写事件后，传送给 MyChannel。
 * 1. 调用 {@link MyChannel#doWrite}触发写事件，经由方法 {@link HandlerContext#write} 传递给 链式调用的
 * 处理器 {@link MyHandler2#write}进行链式处理调用，并经过 {@link HandlerContext#write} 方法有 tail 节点向 head 节点
 * 传递写数据。
 * 2. 写数据最终到达 {@link Pipeline.PipelineHandler#write} 方法中，将 写数据 添加到 ByteBuffer 缓冲队列中。
 * 3. {@link MyHandler2#flush} 方法 依次从 tail 节点 传递到 head 节点，最终到达 {@link Pipeline.PipelineHandler#flush} 方法，
 * 在该方法中 触发了 {@link MyChannel#doFlush()} 方法，将 当前的 selector 监听事件 改成了 OP_WRITE 可写事件。
 * 4. {@link EventLoop2} 监听到 可写事件 后，将消息分发给 {@link MyChannel#write} 方法，最终将消息写入到 Channel 中。
 *
 * @description: 优化 selector  监听事件的 分发
 * @author: wei·man cui
 * @date: 2021/4/21 13:49
 */
public class MyChannel {

    private SocketChannel channel;

    private EventLoop2 eventLoop;

    /**
     * 写消息队列，需要向 channel 中写入消息时，放入队列即可
     */
    private Queue<ByteBuffer> writeQueue = new ArrayBlockingQueue<>(16);

    private Pipeline pipeline;

    public MyChannel(SocketChannel channel, EventLoop2 eventLoop) {
        this.channel = channel;
        this.eventLoop = eventLoop;

        this.pipeline = new Pipeline(this, eventLoop);
        this.pipeline.addLast(new MyHandler1());
        this.pipeline.addLast(new MyHandler2());
    }

    public void doWrite(Object msg) {
        this.pipeline.tailCtx.write(msg);
    }

    /**
     * 读事件 抽象封装
     *
     * @param selectionKey SelectionKey
     */
    public void read(SelectionKey selectionKey) {
        SocketChannel channel = null;
        try {
            channel = (SocketChannel) selectionKey.channel();
            final ByteBuffer buffer = ByteBuffer.allocate(1024);
            final int read = channel.read(buffer);
            if (read == -1) {
                System.out.println("读取到 -1 表示结束，关闭 socket");
                selectionKey.cancel();
                channel.close();
                return;
            }
            this.pipeline.headCtx.fireChannelRead(buffer);
        } catch (IOException e) {
            try {
                System.out.println("读取时发生异常，关闭 Socket");
                channel.close();
                selectionKey.cancel();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void readBak(SelectionKey selectionKey) {
        SocketChannel channel = null;
        try {
            channel = (SocketChannel) selectionKey.channel();
            final ByteBuffer buffer = ByteBuffer.allocate(1024);
            final int read = channel.read(buffer);
            if (read == -1) {
                System.out.println("读取到 -1 表示结束，关闭 socket");
                selectionKey.cancel();
                channel.close();
                return;
            }
            String msg = new String(buffer.array());
            System.out.println("[服务端]接收到客户端消息：" + msg);

            // 一次 消息读取结束后，就向客户端 发送一个 响应
            // 向客户端 发送回执
            writeQueue.add(ByteBuffer.wrap("I have received your msg, this is my response.".getBytes()));
            // 切换 监听事件 为：写事件，之后 EventLoop2 将监听到 写事件
            selectionKey.interestOps(SelectionKey.OP_WRITE);
        } catch (IOException e) {
            try {
                System.out.println("读取时发生异常，关闭 Socket");
                channel.close();
                selectionKey.cancel();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    /**
     * 写事件 抽象分发
     *
     * @param selectionKey selectionKey
     */
    public void write(SelectionKey selectionKey) {
        try {
            ByteBuffer buffer;
            while ((buffer = writeQueue.poll()) != null) {
                final int write = channel.write(buffer);
                System.out.println("写入消息：" + write);
            }
            // 缓冲队列 写完后 在切换为 监听可读事件
            selectionKey.interestOps(SelectionKey.OP_READ);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addWriteQueue(ByteBuffer buffer) {
        this.writeQueue.add(buffer);
    }

    public void flush() {
        this.pipeline.tailCtx.flush();
    }

    /**
     * 刷新缓冲区，将 channel 改为写监听，发送写数据
     */
    public void doFlush() {
        this.channel.keyFor(eventLoop.selector).interestOps(SelectionKey.OP_WRITE);
    }
}
