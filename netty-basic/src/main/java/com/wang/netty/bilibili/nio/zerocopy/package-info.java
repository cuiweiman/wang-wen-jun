/**
 * 传统IO 的 网络文件传输，需要 4 次拷贝、3 次状态切换：
 * 1. 硬盘 经过 DMA 拷贝到 内核 Buffer；
 * 2. 内核 Buffer 经过 CPU 拷贝到 User Buffer （如用户的应用程序中）
 * 3. User Buffer （如用户的应用程序中） 拷贝到 socket 的 Buffer 中
 * 4. socket 的 Buffer 中 拷贝到 Protocol Engine 协议栈。
 * a. 用户态 ——> 内核态
 * b. 内核态 ——> 用户态
 * c. 用户态 ——> 内核态
 * <p> DMA：direct memory access  直接内存拷贝
 * <p>
 * mmap 优化：通过内存映射，将文件映射到内核缓冲区，同时用户空间可以共享内核空间的数据。
 * 这样在网络传输时就减少了内核空间到用户空间的拷贝次数。只有三次拷贝：
 * 1. DMA拷贝，从硬盘到 内核Buffer（user buffer 和 内核 buffer 共享空间）
 * 2. 内核Buffer 经过 CPU 拷贝到 Socket Buffer
 * 3. socket Buffer 通过 DMA 拷贝到 Protocol Engine。
 * <p>
 * <p>
 * 真正的 零拷贝：经过 sendFile 优化后，省略了 CPU 拷贝的步骤。零拷贝即没有CPU拷贝。
 * 1. DMA拷贝，将数据从硬盘拷贝到 kernel buffer（内核 Buffer）
 * 2. DMA拷贝，将数据从 kernel buffer（内核 Buffer）拷贝到 protocol engine
 *
 * @description: 零拷贝：
 * <p>操作系统中设计的零拷贝有 mmap内存映射 和 sendFile
 * @author: wei·man cui
 * @date: 2021/2/23 14:05
 */
package com.wang.netty.bilibili.nio.zerocopy;