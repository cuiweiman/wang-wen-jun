package com.wang.netty.daxin.chapter02.eventloop;

import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 维护了 {@link NioEventLoop}，读写事件的监听注册 和 事件处理 交由 NioEventLoop 来处理
 *
 * @description: 管理 NioEventLoop，初始化 EventLoop 并 构造轮询、事件监听
 * @author: wei·man cui
 * @date: 2021/4/21 18:05
 */
public class NioEventLoopGroup {

    private static final Integer SIZE = 16;

    private NioEventLoop[] eventLoops = new NioEventLoop[SIZE];

    private final AtomicInteger idx = new AtomicInteger();


    /**
     * 初始化
     */
    public NioEventLoopGroup() {
        for (int i = 0; i < SIZE; i++) {
            this.eventLoops[i] = new NioEventLoop();
        }
    }


    /**
     * 轮询 获取 EventLoop。
     * <p>
     * 轮询算法：
     * 1. AtomicInteger 取值并自增的原子操作，可以保证线程安全；
     * 2. 与 容量 15 进行二进制 与操作，结果与 idx % 16 是一致的。
     *
     * @return 轮询获取到的结果
     */
    public NioEventLoop next() {
        return eventLoops[idx.getAndIncrement() & SIZE - 1];
    }

    /**
     * 轮询获取 EventLoop，并向其 注册 监听的 channel 和 事件
     *
     * @param channel 注册监听的channel
     * @param keyOps  注册监听的事件
     */
    public void register(SocketChannel channel, int keyOps) {
        next().register(channel, keyOps);
    }

}
