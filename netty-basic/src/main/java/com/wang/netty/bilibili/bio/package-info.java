/**
 * @description: 传统阻塞IO
 * 面向流的，阻塞的，没有缓存的概念；阻塞意味着，一个线程调用read或write方法时，线程被阻塞，直到数据被读取或被写入。
 * 无法应对高并发的大量请求：会有大量线程被阻塞，一直等待数据，造成系统吞吐量差；
 * 若网络IO阻塞或网络抖动、故障，线程的阻塞时间会更长，造成系统不可靠。因此要使用NIO。
 * @date: 2021/2/21 22:21
 * @author: wei·man cui
 */
package com.wang.netty.bilibili.bio;