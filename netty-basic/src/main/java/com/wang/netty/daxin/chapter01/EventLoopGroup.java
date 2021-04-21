package com.wang.netty.daxin.chapter01;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 主要负责 对 EventLoop 的管理，包括初始化，以及 channel 和 监听事件 的注册
 *
 * @description: EventLoopGroup 管理多个 EventLoop
 * @author: wei·man cui
 * @date: 2021/4/21 13:29
 */
public class EventLoopGroup {

    /**
     * 默认 管理 16 个 EventLoop 数组容量，
     */
    private static final Integer SIZE = 16;

    // private EventLoop[] eventLoops = new EventLoop[SIZE];
    private EventLoop2[] eventLoops = new EventLoop2[SIZE];

    private final AtomicInteger idx = new AtomicInteger();

    /**
     * 初始化
     *
     * @throws IOException 异常
     */
    public EventLoopGroup() throws IOException {
        for (int i = 0; i < SIZE; i++) {
            // this.eventLoops[i] = new EventLoop();
            this.eventLoops[i] = new EventLoop2();
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
    public EventLoop2 next() {
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
