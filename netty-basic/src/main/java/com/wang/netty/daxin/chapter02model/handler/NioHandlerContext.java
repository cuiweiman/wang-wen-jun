package com.wang.netty.daxin.chapter02model.handler;

import com.wang.netty.daxin.chapter02model.channel.NioChannel;

import java.util.Objects;

/**
 * @description: 链式调用的模型
 * @author: wei·man cui
 * @date: 2021/4/21 18:07
 */
public class NioHandlerContext {

    private NioHandler handler;

    private NioChannel channel;

    public NioChannel getChannel() {
        return channel;
    }

    /**
     * 双向链表
     */
    public NioHandlerContext prev;
    public NioHandlerContext next;

    public NioHandlerContext(NioHandler handler, NioChannel channel) {
        this.handler = handler;
        this.channel = channel;
    }

    public void fireChannelRead(Object msg) {
        NioHandlerContext n = this.next;
        if (Objects.nonNull(n)) {
            n.handler.channelRead(n, msg);
        }
    }


    /**
     * 链式调用 从 tail 到 head
     *
     * @param msg 消息
     */
    public void write(Object msg) {
        NioHandlerContext p = this.prev;
        if (Objects.nonNull(p)) {
            p.handler.write(p, msg);
        }
    }

    /**
     * 链式调用，从 tail 到 head
     */
    public void flush() {
        NioHandlerContext p = this.prev;
        if (Objects.nonNull(p)) {
            p.handler.flush(p);
        }
    }
}
