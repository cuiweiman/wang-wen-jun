package com.wang.netty.daxin.chapter05rpc.rpcclient;

import com.wang.netty.daxin.chapter05rpc.pojo.RpcRequest;
import com.wang.netty.daxin.chapter05rpc.pojo.RpcResponse;
import com.wang.netty.daxin.chapter05rpc.pojo.SyncResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: Java对象 处理器
 * @author: wei·man cui
 * @date: 2021/4/25 15:26
 */
@Slf4j
public class RpcClientMsgHandler extends ChannelInboundHandlerAdapter {

    private Channel channel;

    private Map<String, SyncResponse> syncMap = new ConcurrentHashMap<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("[客户端 RpcClientMsgHandler#channelRead]接收到数据" + msg.toString());
        // 获取 生产者 响应的数据，唤醒阻塞的 {@link RpcServiceProxyHandler.invoke} 线程。
        RpcResponse response = (RpcResponse) msg;
        final SyncResponse syncResponse = syncMap.remove(response.getRespId());
        syncResponse.setResult(response.getResult());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 建立连接，保存 Channel
        log.debug("[客户端 RpcClientMsgHandler#channelActive] 客户端连接建立");
        channel = ctx.channel();
        /*User user = new User(true);
        ctx.writeAndFlush(user);*/
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("[客户端 RpcClientMsgHandler#channelInactive] 客户端连接断开");
        channel = null;
    }

    /**
     * 通过 Netty，发送 RPC 请求信息
     */
    public SyncResponse sendRpcRequest(RpcRequest rpcRequest) {
        if (Objects.isNull(channel)) {
            throw new RuntimeException("Channel 已关闭");
        }
        // 构建一个 同步响应器
        final SyncResponse syncResponse = new SyncResponse(rpcRequest.getReqId());
        syncMap.put(rpcRequest.getReqId(), syncResponse);

        channel.writeAndFlush(rpcRequest);
        return syncResponse;
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("[客户端 RpcServerMsgHandler#exceptionCaught]发生异常： {}", cause.toString());
        ctx.close();
    }
}
