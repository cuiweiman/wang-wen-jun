package com.wang.netty.daxin.chapter05rpc.pojo;

import com.wang.netty.daxin.chapter05rpc.rpcclient.RpcClientMsgHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 消费者 只有 RpcService 的API接口，而没有其实现类，因此要使用 {@link InvocationHandler#invoke}
 * 进行代理，获取到调用的接口信息，传送给 生产者 调用实际的服务实现类，调用结束后再向 消费者 写回 调用结果。
 *
 * @description: {@link RpcService} 的代理类
 * @author: wei·man cui
 * @date: 2021/4/25 16:15
 */
public class RpcServiceProxyHandler implements InvocationHandler {

    private RpcClientMsgHandler handler;

    public RpcServiceProxyHandler(RpcClientMsgHandler handler) {
        this.handler = handler;
    }

    /**
     * 调用 Netty，将 消费者 的 接口调用信息 发送到 生产者。
     * {@code handler.sendRpcRequest(request);} 交由 Netty 进行调用信息发送。
     * <p>
     * 由于 Netty 是异步的，会直接向下执行，直接执行到 {@code return new User()} 而不等待 netty
     * 调用服务后返回执行结果。
     * <p>
     * 通过 使用 Lock.lock 进行加锁，condition.await 进行阻塞来解决。
     * {@link RpcClientMsgHandler#channelRead} 读到 netty 异步返回的执行结果后，唤醒阻塞的线程。
     *
     * @param proxy  proxy
     * @param method 方法信息
     * @param args   入参值
     * @return 结果
     * @throws Throwable 异常
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 构造 RPC 请求实体类信息，封装了请求参数
        RpcRequest request = new RpcRequest();
        request.setReqId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParamsClass(method.getParameterTypes());
        request.setParams(args);

        // 通过 Netty 发送服务调用数据
        final SyncResponse syncResponse = handler.sendRpcRequest(request);


        // 由于 Netty 是异步请求，会直接向下继续执行，返回一个 空的 User。
        // 待 生产者执行结束后，也会通过 Netty 再返回执行结果。
        // 总之，需要阻塞当前线程，直到 生产者返回执行结果
        return syncResponse.getResult();
        // return new User();
    }
}
