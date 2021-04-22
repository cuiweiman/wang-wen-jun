package com.wang.netty.daxin.chapter02model.channel;

import com.wang.netty.daxin.chapter02model.eventloop.NioEventLoop;
import com.wang.netty.daxin.chapter02model.handler.NioHandler1;
import com.wang.netty.daxin.chapter02model.handler.NioHandler2;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 负责 处理 IO的 读写 事件，将 NioEventLoop 的读写事件进行解耦，
 * NioEventLoop 只进行事件分发 和 监听事件的注册。
 * <p>
 * 加入了 {@link NioPipeline} 链式调用
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

        this.pipeline = new NioPipeline(this, eventLoop);
        this.pipeline.addLast(new NioHandler1());
        this.pipeline.addLast(new NioHandler2());
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


    public void doWrite(Object msg) {
        this.pipeline.tailCtx.write(msg);
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
