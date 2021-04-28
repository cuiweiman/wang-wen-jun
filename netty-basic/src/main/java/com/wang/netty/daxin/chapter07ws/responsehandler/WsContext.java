package com.wang.netty.daxin.chapter07ws.responsehandler;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * ChannelGroup 群发
 *
 * @description: 存储 客户端连接的 Channel，方便发送消息
 * @author: wei·man cui
 * @date: 2021/4/28 11:30
 */
public class WsContext {

    /**
     * 存储 Channel 对应的 用户的 ID
     */
    public static final AttributeKey<String> USER_ID = AttributeKey.valueOf("USER_ID");

    /**
     * 存储 所有的 客户端 Channel，内部是一个 ConcurrentHashMap。
     */
    public static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

}
