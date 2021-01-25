package com.wang.think.rmi;

/**
 * <p>
 * Java 远程方法调用 RMI，是用于实现 远程过程调用 的 应用程序编程接口。使得客户机运行的程序可以调用远程服务器上的对象服务。
 * 远程方法调用特性使得Java 编程人员能够在网络环境中 分布操作。RMI 全部的宗旨是：尽可能地简化远程接口对象的调用。
 * <p>
 * Java RMI 极大地依赖于接口。在创建一个远程对象时，程序员通过传递一个接口来隐藏底层的实现细节。
 * 客户端得到的远程对象句柄 正好与本地的根代码连接，由后者负责透过网络通信。
 * 如此，程序员只需要关心如何通过自己的接口句柄发送消息。
 *
 * @description: 远程服务 接口 Demo
 * @author: wei·man cui
 * @date: 2021/1/25 15:20
 */
public interface HelloRmiService {

    int getAdd(int a, int b);

}
