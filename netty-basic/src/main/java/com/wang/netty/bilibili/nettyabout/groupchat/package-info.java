/**
 * 在 Handler 中，包含 因事件发生而触发各种方法，因此需要掌握 ChannelHandler 和 Channel 的生命周期，以及触发时机。
 * <p>
 * Channel 的生命周期： ChannelInboundHandlerAdapter 管理
 * 1. {@link io.netty.channel.ChannelInboundHandlerAdapter#channelRegistered(io.netty.channel.ChannelHandlerContext)}：Channel 被注册到 EventLoop 时触发。
 * 2. {@link io.netty.channel.ChannelInboundHandlerAdapter#channelActive(io.netty.channel.ChannelHandlerContext)}：Channel 处于活动状态，已连接到远程节点，可以收发消息时，触发。
 * 3. {@link io.netty.channel.ChannelInboundHandlerAdapter#channelInactive(io.netty.channel.ChannelHandlerContext)}：Channel 失去远程节点的连接时 触发。
 * 4. {@link io.netty.channel.ChannelInboundHandlerAdapter#channelUnregistered(io.netty.channel.ChannelHandlerContext)}：Channel已经被创建，但还未注册到 EventLoop 或已从 EventLoop 中注销时 触发。
 * 5. {@link io.netty.channel.ChannelInboundHandlerAdapter#exceptionCaught(io.netty.channel.ChannelHandlerContext, java.lang.Throwable)}：Channel 捕获到异常
 * </p>
 * ChannelHandler 生命周期： ChannelHandlerContext 管理
 * 1. {@link io.netty.channel.ChannelHandlerAdapter#handlerAdded(io.netty.channel.ChannelHandlerContext)}：ChannelHandler 添加到 ChannelPipeline 时 触发；
 * 2. {@link io.netty.channel.ChannelHandlerAdapter#handlerRemoved(io.netty.channel.ChannelHandlerContext)}：ChannelPipeline 移除 ChannelHandler 时 触发。
 * <p>
 * <p> 事件触发 执行顺序
 * channelRegistered() ——> handlerAdded() ——> channelActive() ——> channelInactive() ——> handlerRemoved() ——> channelUnregistered()
 *
 * @description: 简单的 客户端与服务端 聊天室
 * @author: wei·man cui
 * @date: 2021/2/24 17:42
 */
package com.wang.netty.bilibili.nettyabout.groupchat;