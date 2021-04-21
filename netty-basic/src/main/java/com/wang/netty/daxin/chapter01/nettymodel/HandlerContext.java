package com.wang.netty.daxin.chapter01.nettymodel;

import com.wang.netty.daxin.chapter01.MyChannel;

import java.util.Objects;

/**
 * 链式调用的模型 https://cuiweiman.github.io/images/netty/01-pipeline.png
 *
 * @description: 优化 MyChannel 的事件处理
 * @author: wei·man cui
 * @date: 2021/4/21 15:09
 */
public class HandlerContext {

    private Handler handler;

    private MyChannel myChannel;

    public MyChannel getMyChannel() {
        return myChannel;
    }

    /**
     * 双向链表
     */
    HandlerContext prev;
    HandlerContext next;

    public HandlerContext(Handler handler, MyChannel myChannel) {
        this.handler = handler;
        this.myChannel = myChannel;
    }

    /**
     * 链式调用 从 head 到 tail
     *
     * @param msg 消息
     */
    public void fireChannelRead(Object msg) {
        // 使用 head 的下一个 HandlerContext 进行 事件处理
        HandlerContext n = this.next;
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
        HandlerContext p = this.prev;
        if (Objects.nonNull(p)) {
            p.handler.write(p, msg);
        }
    }

    /**
     * 链式调用，从 tail 到 head
     */
    public void flush() {
        HandlerContext p = this.prev;
        if (Objects.nonNull(p)) {
            p.handler.flush(p);
        }
    }


}
