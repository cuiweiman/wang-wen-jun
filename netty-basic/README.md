
> [TOC]

**[大新学堂 Netty 教程](https://edu.51cto.com/course/23106.html)**

**[Netty部分功能的源码专栏](https://blog.csdn.net/nimasike/category_8665668.html)**

**[项目源码：netty-basic/netty-boot](https://github.com/cuiweiman/wang-wen-jun)**

# Netty 参数配置说明

> ==ChannelOption.SO_BACKLOG==：对应的是 tcp/ip 协议 listen 函数中的 backlog 参数，函数 listen(int socketfd,int backlog) 用来初始化服务端可连接队列，服务端处理客户端连接请求是顺序处理的，所以同一时间只能处理一个客户端连接，多个客户端来的时候，服务端将不能处理的客户端连接请求放在队列中等待处理，backlog参数指定了队列的大小

## 服务端 ServerBootstrap 参数配置
|配置内容|配置作用|
|:--|:--|
|handler()/option()|bossGroup 主 EventLoopGroup 配置参数|
|childHandler()/childOption()|workGroup 副 EventLoopGroup 配置参数|
|group(bossGroup, workGroup)|配置主副NioEventLoopGroup|
|handler(new LoggingHandler(LogLevel.DEBUG))|配置日志打印|
|channel(NioServerSocketChannel.class)|配置服务端 SocketChannel 类型|
|option(ChannelOption.SO_BACKLOG, 1024)|临时存放已完成三次握手的请求的队列的最大长度,默认50|
|option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)|重用缓冲区|
|childHandler(new LoggingHandler(LogLevel.DEBUG))|子EventLoopGroup配置日志|
|childOption(NioChannelOption.SO_RCVBUF, 8 * 1024)|接收缓冲区大小|
|childOption(NioChannelOption.SO_SNDBUF, 8 * 1024)|发送缓冲区大小|
|childOption(NioChannelOption.SO_KEEPALIVE, true)|开启TCP长连接|
|childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)|子EventLoopGroup配置日志配置重用缓冲区|
|childHandler(new ServerChannelInitializer())|添加配置Handler处理器|

## 客户端 Bootstrap 参数配置
|配置内容|配置作用|
|:--|:--|
|group(eventLoopGroup)|客户端配置EventLoopGroup|
|handler(new LoggingHandler(LogLevel.DEBUG))|配置日志打印|
|channel(NioSocketChannel.class)|配置客户端 SocketChannel 类型|
|option(ChannelOption.SO_BACKLOG, 1024)|临时存放已完成三次握手的请求的队列的最大长度,默认50|
|option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)|重用缓冲区|
|option(NioChannelOption.SO_RCVBUF, 8 * 1024)|接收缓冲区大小|
|option(NioChannelOption.SO_SNDBUF, 8 * 2014)|发送缓冲区大小|
|option(NioChannelOption.SO_KEEPALIVE, true)|长连接|
|handler(new ClientChannelInitializer())|添加配置Handler处理器|


# 预备知识
## Wireshark 抓包工具


## Java 堆内内存与堆外内存
- **堆内存**：heap memory 普通内存；受 JVM-GC 管理，会根据情况移动或释放内存，无需人工干预。
- **堆外内存**：non-heap memory，直接内存，非堆内存；不受 JVM-GC管理，需要手动释放，不释放会导致内存泄漏。


### Unsafe 操作堆外内存
```java
public static void main(String[] args) {
    Unsafe unsafe = getUnsafe();
    // 分配 10 字节的内存，返回值为 内存寄出地址
    long address = unsafe.allocateMemory(10);
    // 传入 基础地址，长度为10，byte-0，初始化堆外内存
    unsafe.setMemory(address, 10L, (byte) 0);
    // 传入内存地址，设置 byte 值
    unsafe.putByte(address, (byte) 1);
    // 根据内存值，获取 byte值
    System.out.println("申请到的内存地址：address=" + address);
    System.out.println(unsafe.getByte(address));
    unsafe.freeMemory(address);
}
private static Unsafe getUnsafe() {
    try {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        return (Unsafe) field.get(null);
    } catch (NoSuchFieldException | IllegalAccessException e) {
        throw new RuntimeException(e);
    }
}
```

### NIO 的 ByteBuffer 操作内存
ByteBuffer 可以操作 堆内内存 和 堆外内存(即：直接内存)：
- **HeapBuffer：堆内内存**，内部使用 byte[] 存取数据；
    ```java
    // 申请创建 堆内内存 HeapByteBuffer
    final ByteBuffer allocate = ByteBuffer.allocate(16);
    allocate.putInt(8855666);
    allocate.put((byte) 127);
    allocate.flip();
    System.out.println(allocate.getInt());
    System.out.println(allocate.get());
        
    // 底层：
    public static ByteBuffer allocate(int capacity) {
        if (capacity < 0)
            throw new IllegalArgumentException();
        return new HeapByteBuffer(capacity, capacity);
    }
    ```
- **DirectBuffer：堆外内存/直接内存**，内部使用 Unsafe 存取数据；
    ```java
    final ByteBuffer allocateDirect = ByteBuffer.allocateDirect(16);
    allocateDirect.put((byte) 25);
    allocateDirect.flip();
    System.out.println(allocateDirect.get());
    // DirectByteBuffer 内部的 java.nio.DirectByteBuffer.Deallocator 线程 
    // 进行内存回收，通过 调用 Cleaner 清理创建的堆外内存 对象
    allocateDirect.clear();
    // 底层：
    public static ByteBuffer allocateDirect(int capacity) {
        return new DirectByteBuffer(capacity);
    }
    ```

**ByteBuffer 底层原理**
> - position：指示 缓冲区 读/写 的位置。
> - limit：缓冲区 缓存的数据 容量。
> - capacity：缓冲区容量的最大值。

1. 初始状态：position 默认为0，向缓冲区写入数据时，下一个写入的数据就进入 slot 0 的位置；如果是从缓冲区读取数据，下一个读取的位置就在 slot 0。
2. 第一次写入：向缓冲区中写入 3 个 字节，position 增加到 3。
3. 第二次写入：向缓冲区写入 3 个字节，那么 position 再加 2。
4. 写入结束，读取数据，要调用 ==flip()== 方法，转换读写
    - 将 limit 设为 当前 position（读取时的终止位置）；
    - 将 position 设为 0；
5. 第一次读取：读取 4 个字节，
6. ==clear()== 方法重置缓冲区，以便重新使用
    - 将 limit 设置与 capacity 相同；
    - 设置 position 为 0。

> 大并发场景下，需要频繁地 创建、销毁 内存空间，非常消耗资源，损耗性能。不如 Netty 完美。


## BIO 模型
传统阻塞IO，基于流模型实现的，同步、阻塞，数据在可读之前、可写之前 会一直阻塞。代码实现比较简单、直观，但要占用大量线程，易成为系统性能瓶颈。

### ServerSocket 服务端套接字
```java
public static void main(String[] args) throws IOException {
    ThreadPoolExecutor executor = ExecutorUtils.getExecutor();
    ServerSocket serverSocket = new ServerSocket(9090);
    while (true) {
        System.out.println("[服务端]等待客户端连接……");
        // 监听 客户端 连接，若没有连接，线程会 阻塞 在这里
        final Socket socket = serverSocket.accept();
        System.out.println("[服务端]客户端连接成功……" + socket.getRemoteSocketAddress());
        executor.execute(new ClientHandler(socket));
    }
}

public static class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        int readNum = 0;
        byte[] content = new byte[1024];
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {
            while ((readNum = inputStream.read(content)) != -1) {
                System.out.println(new String(Arrays.copyOf(content, readNum)));
                outputStream.write("server get it".getBytes(StandardCharsets.UTF_8));
            }
            socket.close();
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException(e);
        }
    }
}
```

> OutputStream#write(byte[]);  如果系统特别繁忙，流量特别大，可能会导致 缓冲区满，导致数据无法写入，直到 缓冲区数据 被处理(读/写)。

## 模拟 Netty 的 EventLoopGroup 线程模型
- 服务端的 ServerSocketChannel 向 selector 注册一个 连接监听 事件，管理 客户端的连接事件。接收到 客户端的连接后，向连接的 SocketChannel 注册一个 读事件 的监听。
- EventLoopGroup 管理 所有的 EventLoop，在 EventLoop 中 管理读写事件的注册。每个 EventLoop 对应一个线程、对应一个 selector 和 一个 SocketChannel。
- EventLoopGroup 主要管理了 EventLoop 数组/集合，对 EventLoop 进行轮询，将客户端连接均匀的分配给各个 EventLoop 中管理的 Selector。
- EventLoop 中负责 对 channel 读写事件的注册和监听，使用多线程的方式进行监听，并对监听到的事件进行分发，事件统一由 MyChannel 处理，EventLoop 只是对监听到的 事件进行分发，分发给 MyChannel。


## 模拟 Netty 的 Pipeline 线程模型

> 主要是对 MyChannel 的事件处理，进行优化。Netty 的 pipeline 链式处理：
> - 当 MyChannel 读到数据时，data 从 head 头部开始传递，经过多个 HandlerContext，最终传递到 tail 尾部。
> - 当 MyChannel 写数据时，data 从 tail 尾部开始传递，经过多个 HandlerContext，最终传递到 head 头部。

![pipeline](https://cuiweiman.github.io/images/netty/01-pipeline.png)



1. ServerSocketChannel 向 selector 注册 监听 客户端的 连接事件
2. EventLoopGroup 管理 EventLoop 线程，负责 将 SocketChannel 的 读写事件 注册到 selector 中。
3. EventLoop2 监听到 读写事件后，将 数据 分发给 MyChannel 处理。
4. MyChannel 中的 Pipeline 维护了 链式处理器，最终数据交由 链式处理器中的 各个处理器节点一次处理。

> [github项目地址](https://github.com/cuiweiman/wang-wen-jun) com.wang.netty.daxin.chapter01.NioServerSocketChannel3

```java
/**
 * 最终优化结果：
 * 1. ServerSocketChannel 向 selector 注册 监听 客户端的 连接事件
 * 2. {@link EventLoopGroup} 管理 EventLoop 线程，负责 将 SocketChannel 的 读写事件 注册到 selector 中。
 * 3. {@link EventLoop2} 监听到 读写事件后，将 数据 分发给 {@link MyChannel}处理。
 * 4. {@link MyChannel} 中的 Pipeline 维护了 链式处理器，最终数据交由 链式处理器中的 各个处理器节点一次处理。
 */
 
/**
 * Channel 负责 处理 IO的读写 事件，事件由 EventLoop2 根据读写事件进行分发
 * <p>
 * 最终优化：{@link Pipeline} {@link Handler} {@link HandlerContext}
 *
 * <p>
 * 读数据 事件：EventLoop2 监听到 可读事件后，传送给 MyChannel。
 * {@link MyChannel#read} 将数据 经过方法 {@link HandlerContext#fireChannelRead} 传递给
 * 链式调用的处理器 {@link MyHandler1#channelRead}进行链式处理调用，在链式处理过程中，通过
 * {@link HandlerContext#fireChannelRead} 方法 进行 消息的链式传递。{@link Pipeline} 中维护了链式处理器的顺序。
 *
 * <p>
 * 写数据 事件： EventLoop2 监听到 可写事件后，传送给 MyChannel。
 * 1. 调用 {@link MyChannel#doWrite}触发写事件，经由方法 {@link HandlerContext#write} 传递给 链式调用的
 * 处理器 {@link MyHandler2#write}进行链式处理调用，并经过 {@link HandlerContext#write} 方法有 tail 节点向 head 节点
 * 传递写数据。
 * 2. 写数据最终到达 {@link Pipeline.PipelineHandler#write} 方法中，将 写数据 添加到 ByteBuffer 缓冲队列中。
 * 3. {@link MyHandler2#flush} 方法 依次从 tail 节点 传递到 head 节点，最终到达 {@link Pipeline.PipelineHandler#flush} 方法，
 * 在该方法中 触发了 {@link MyChannel#doFlush()} 方法，将 当前的 selector 监听事件 改成了 OP_WRITE 可写事件。
 * 4. {@link EventLoop2} 监听到 可写事件 后，将消息分发给 {@link MyChannel#write} 方法，最终将消息写入到 Channel 中。
 */
```

# Netty 常用工具类

## ByteBuf
原理基本与 JDK 的ByteBuffer 相同，也分为 堆外、堆内内存，并且还分为 池化、非池化。但是做了极大地优化，API 更多更简单，内存池设计更优秀。

> - 池化：Netty 默认。减少了内存复制和GC，性能更好。
> - 非池化：需要 内存空间 频繁地创建和销毁；

Netty中，池化的 内存池 默认容量为 16M。**ByteBuf 使用完毕后一定要记得释放，否则会造成内存泄漏。**

```java
// 堆内 池化内存，直接获取 1个 page 的内存空间
final ByteBuf pooledHeapBuffer = PooledByteBufAllocator.DEFAULT.heapBuffer(8192);
// 堆外 池化内存
final ByteBuf pooledDirectBuffer = PooledByteBufAllocator.DEFAULT.directBuffer(8192);


final ByteBuf pooledHeapBuffer = PooledByteBufAllocator.DEFAULT.heapBuffer(10);
System.out.println(pooledHeapBuffer.refCnt());
// ByteBuf 创建后 引用计数 为 1，又增加了 两次，减少了 一次。
pooledHeapBuffer.retain();
pooledHeapBuffer.retain();
pooledHeapBuffer.release();
System.out.println("引用个数：" + pooledHeapBuffer.refCnt());
```
> com.wang.netty.daxin.chapter03.ByteBufTest

**ByteBuf 与 String 的相互转换**
```java
// String类型的 msg，写入 ByteBuf
final ByteBuf byteBuf = ctx.alloc().buffer(32);
String data = (String) msg;
byteBuf.writeBytes(data.getBytes(StandardCharsets.UTF_8));

// ByteBuf 类型转换成 String 字符串
String data = buf.toString(StandardCharsets.UTF_8);
```

## NioEventLoop
> io.netty.channel.nio.NioEventLoop

- 对 Selector 的优化：NioEventLoop#NioEventLoop 构造方法
- rebuildSelector 解决 Epoll 空轮询 ：NioEventLoop#run
    
    > 在Linux 内核 2.6 版本下，while 轮询中的 int select = selector.select(); 不会发生阻塞，会直接向下继续执行，while 循环空转，导致死循环。
- IO事件处理流程：NioEventLoop#run，NioEventLoop#processSelectedKeys


## NioEventLoopGroup 处理用户任务
> 仅限处理一些 执行比较快的任务，不要占用 EventLoopGroup 线程太久，否则 EventLoopGroup 不能处理下一个任务，导致阻塞。因为 **一个 EventLoopGroup 只有一个线程**。

```java
public static void main(String[] args) {
    final NioEventLoopGroup loopGroup = new NioEventLoopGroup(1);
    // 一个普通的任务，立即执行，只执行一次
    loopGroup.submit(() -> {
        log.debug("submit");
    });
    // 延迟任务，可以设置延迟时间，只执行一次
    loopGroup.schedule(() -> {
        log.debug("schedule 10 Seconds");
    }, 10, TimeUnit.SECONDS);

    // 延迟周期任务，可以设置延迟时间和执行间隔，重复执行。
    loopGroup.scheduleAtFixedRate(() -> {
        log.debug("延迟周期性任务，延迟时间为 {} 秒，执行周期为 {} 秒", 5, 5);
    }, 5, 5, TimeUnit.SECONDS);
}
```

## Pipeline
- 一个客户端连接，对应一个 Channel，一个 Channel 对应 一个 Pipeline；
- Pipeline 中有两个变量，一个指向 headHandler，一个指向 tailHandler；
- Pipeline 管理了一系列的 Handler，总体分为  InboundHandler、 OutboundHandler。

## ChannelFuture 的事件监听
```java
// 监听消息写入是否成功
final ChannelFuture future = ctx.channel().writeAndFlush(Unpooled.wrappedBuffer("abc".getBytes()));
future.addListener((channelFuture) -> {
    if (channelFuture.isSuccess()) {
        log.info("写入成功");
    } else {
        log.info("写入失败");
    }
});

// 监听服务端是否关闭端口
ChannelFuture future = bootstrap.bind(new InetSocketAddress(9090)).sync();
future.channel().closeFuture().addListener(channelFuture -> {
    workGroup.shutdownGracefully();
    bossGroup.shutdownGracefully();
});
```

## Sync同步操作
> Netty 中线程都是异步执行的。例如在绑定端口时，若不适用同步，则可能直接继续向后执行。

```java
bootstrap.bind(new InetSocketAddress(9090)).sync();
```


## Netty 入站和出站 In/OutboundHandler
### Channel**HandlerAdapter
- ChannelInboundHandlerAdapter：处理入站的数据
- ChannelOutboundHandlerAdapter: 处理出站的数据
- ChannelDuplexHandler：处理出站和入站的数据

> 处理入站数据时，若 未在自定义 的 Handler 中释放 ByteBuf 数据，那么 DefaultChannelPipeline#channelRead 方法会在判断后进行释放。而不必担心内存泄漏。

```java
@Override
public void channelRead(ChannelHandlerContext ctx, Object msg) {
    System.out.println("SecondHandler " + msg);
    // ctx.channel().write("I received your msg.");
    // ctx.channel().flush();
    ctx.channel().writeAndFlush("I received your msg.");
}

@Override
public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
    final ByteBuf byteBuf = ctx.alloc().buffer(32);
    String data = (String) msg;
    byteBuf.writeBytes(data.getBytes(StandardCharsets.UTF_8));
    ctx.write(byteBuf);
}

@Override
public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    // Close the connection when an exception is raised.
    cause.printStackTrace();
    ctx.close();
}
```

### Channel**HandlerAdapter 常用回调事件
- **ChannelHandlerAdapter#handlerAdded()**：当本 Handler 添加到 Pipeline 上时触发回调。
- **ChannelInboundHandlerAdapter#channelRegistered()**：当一个SocketChannel 注册到 Selector 上时触发回调；
- **ChannelInboundHandlerAdapter#channelActive()**：TCP建立连接后触发；
- **ChannelHandlerAdapter#exceptionCaught()**：客户端关闭连接时，或者任何回调方法出现异常时，触发异常回调。
- **ChannelInboundHandlerAdapter#channelInActive()**：TCP关闭连接后触发；
- **ChannelInboundHandlerAdapter#channelUnregistered()**：当一个SocketChannel 取消注册 Selector 时触发回调；
- **ChannelHandlerAdapter#handlerRemoved()**：当本 Handler 从 Pipeline 上移除时触发回调。

> 前三个按顺序触发回调。


### SimpleChannelInboundHandler
**相比 ChannelInboundHandlerAdapter：**
- 使用了 泛型，不需要手动进行 强制类型转换；
- 对 ByteBuf 的内存空间进行了自动释放；
- 二者区别不是很大，只是简化了一些方法代码。


## StringEncoder 和 StringDecoder
> 两个现成的编解码工具类，可以实现 String->ByteBuf，和 ByteBuf->String 的转换。加入到 Pipeline 之后，就不需要在 自定义入站处理器的 channelRead 方法 和 出站处理器的 write 方法中进行 ByteBuf类型——String类型 的转换了。

```java
// StringDecoder 继承自 入站处理器类，加入后即可不用再写 ByteBuf 转 String 的处理逻辑了
ch.pipeline().addLast("StringDecoder", new StringDecoder(StandardCharsets.UTF_8));
// StringEncoder 继承自 出站处理器类，加入后 不用再写 String 转 ByteBuf 的处理逻辑
ch.pipeline().addLast("StringEncoder", new StringEncoder(StandardCharsets.UTF_8));
```

## ObjectEncoder 和 ObjectDecoder
> 两个现成的编解码工具类，可以实现 Java对象 的序列化和反序列化，从而实现 Netty 传输 Java 对象的功能。体现在对 RPC 协议的实现中。

```java
.childHandler(new ChannelInitializer<SocketChannel>() {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("ObjectDecoder", new ObjectDecoder(Integer.MAX_VALUE,
                ClassResolvers.weakCachingResolver(null)));
        ch.pipeline().addLast("ObjectEncoder", new ObjectEncoder());
        ch.pipeline().addLast(…………);
    }
});
```
com.wang.netty.daxin.chapter04netty


## TCP的拆包和粘包

### TCP报文
> TCP 属于运输层协议。

### Netty中 粘包拆包 的工具类

- LineBasedFrameDecoder：分隔符粘包，只限于 \r\n 分隔符
- FixedLengthFrameDecoder：固定长度粘包
- **LengthFieldBasedFrameDecoder**：先读取到消息的长度，在读取该长度的数据，长度过了则拆包，长度不到则缓存等待下一个TCP数据包。
- **DelimiterBasedFrameDecoder：另一种设置粘包分隔符的方式，可以自定义分隔符**

```java
// LineBasedFrameDecoder：粘包 分隔符，只限于 对 \r\n 换行分隔符的拆分，例如客户端消息：……I miss you.\r\n
ch.pipeline().addLast("LineBasedFrameDecoder", new LineBasedFrameDecoder(2048 * 1024));


// FixedLengthFrameDecoder：固定长度为 5 的粘包分隔符
ch.pipeline().addLast("FixedLengthFrameDecoder", new FixedLengthFrameDecoder(5));

// 切分消息：根据 lengthFieldOffset 到 lengthFieldLength 截取到 消息（消息开头是16进制数，表示了后面数据的长度）的长度，然后根据长度向后读取指定长度的数据。
// lengthFieldOffset：读过1个字节，才是length起始位置；
// lengthFieldLength：length占用两个字节/4位；
// lengthAdjustment：需要再读取 n+(-3)个字节，就是一个完整的消息
// initialBytesToStrip：读取完后丢弃3个字节。
ch.pipeline().addLast("LengthFieldBasedFrameDecoder", new LengthFieldBasedFrameDecoder(
Integer.MAX_VALUE, 1, 2, -3, 3));

// DelimiterBasedFrameDecoder：可以自定义分隔符
// ByteBuf delimiter = Unpooled.copiedBuffer("]".getBytes());
ByteBuf delimiter = Unpooled.wrappedBuffer("]".getBytes());
ch.pipeline().addLast(new DelimiterBasedFrameDecoder(2048 * 1024, delimiter));
```

> 当 粘包长度不足、或者 没有读到 粘包 的 结束分隔符 时，数据会进行缓存，缓存在 ByteToMessageDecoder 类中。ByteToMessageDecoder 是多个解码器/拆包工具的父类。


# Netty 实现简单的RPC协议
- **服务消费者：** 
    - 服务消费者 需要使用和 生产者同样的 服务API接口，但不需要实现具体的方法类。
    - 消费者实际进行 远程服务调用时，发起请求并不是直接到达生产者，而是到达JDK的代理类，代理类代理了 远程服务接口 的请求方法。
- **JDK代理：**
    - 代理了 消费者调用的远程服务接口，将请求重定向到配置的 代理处理器类 中。
    ```java
    public RpcService getRpcService() {
        // 生成 JDK 的动态代理。{@code RpcServiceProxyHandler}动态代理{@code RpcService}接口，使得没有接口实现类也可以调用。
        // 当然 RpcService 并非真的没有实现，只是在 消费者Client 端没有实现，在生产者 Procedure 端实现了。
        return (RpcService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{RpcService.class},
                new RpcServiceProxyHandler(handler)
        );
    }
    ```
- **代理 远程服务的处理器类**
    - 需要实现 InvocationHandler#invoke 接口和方法；
    - 接收到消费者的请求信息（方法信息以及参数信息），并封装成一个 请求实体类；
    - 通过 Netty 客户端，将 请求实体类数据 发送给 Netty 服务端；
    - 由于 Netty 是异步的，因此 线程 将请求发送给服务端后，会直接向下执行而无法获取到服务端的执行结果；
    - 因此构建一个阻塞线程的 响应实体类，进行线程阻塞，当 Netty 服务端返回调用信息时再唤醒阻塞的线程。

- **Netty客户端发送请求信息**
    - 客户端建立服务端连接后，记录下连接的channel通道，用于传送 rpc 请求数据。并断开连接后清空channel。
    - 代理类调用 发送RPC数据的方法后，将请求数据通过 netty 发送到服务端，并返回 具有阻塞功能的 同步响应实体类。
    - 通过 同步响应实体类 获取生产者执行结果时，进行线程阻塞，等待 netty 服务端返回执行数据后 再唤醒线程。

- **Netty服务端接收请求信息**
    - netty 服务端缓存所有的 服务类信息（也可以从Spring的BeanFactory中获取）；
    - 接收到 客户端发送的请求信息后，使用 反射机制 执行方法，拿到执行结果；
    - netty 服务端 封装执行结果，再传输给 netty 客户端；
- **Netty客户端接收执行结果**
    - Netty 客户端读取服务端发送的执行结果数据，并封装成 同步响应实体类。
    - 在封装 同步响应实体类 时，唤醒阻塞的线程，获取执行结果，RPC调用结束。


> 具体代码实现：com.wang.netty.daxin.chapter05rpc。



# Netty 实现 WebSocket
## Netty 对 Http 协议的编解码器
> Http 协议属于应用层协议

[Netty 官方Demo：io.netty.example.http.helloworld](https://github.com/netty/netty/blob/4.1/example/src/main/java/io/netty/example/http/helloworld/HttpHelloWorldServer.java)

```java
// 处理 Http 请求的编解码器——请求 解码器
ch.pipeline().addLast("HttpRequestDecoder", new HttpRequestDecoder());
// 处理 Http 请求的编解码器——响应 编码器
ch.pipeline().addLast("HttpResponseEncoder", new HttpResponseEncoder());
// 当一个 Http 请求比较大时，解码器对报文进行分块解析，解析一部分存储在 aggregator聚合器中，
// 直至报文解析结束，聚合器继续向下传递数据。
ch.pipeline().addLast("aggregator", new HttpObjectAggregator(655360));
ch.pipeline().addLast("HttpServerHandler", new HttpServerHandler());
```

## WebSocket 服务
### Netty搭建WebSocket服务端
> Http协议只能由客户端发起；WebSocket 既可以客户端发送，也可以由服务端推送。

```java
protected void initChannel(SocketChannel ch) throws Exception {
    // 处理 Http 请求的编解码器——请求 解码器
    ch.pipeline().addLast("HttpRequestDecoder", new HttpRequestDecoder());
    // 处理 Http 请求的编解码器——响应 编码器
    ch.pipeline().addLast("HttpResponseEncoder", new HttpResponseEncoder());
    // 当一个 Http 请求比较大时，解码器对报文进行分块解析，解析一部分存储在 aggregator聚合器中，
    // 直至报文解析结束，聚合器继续向下传递数据。
    ch.pipeline().addLast("aggregator", new HttpObjectAggregator(655360));

    // 配置 WebSocket 的编解码器
    final WebSocketServerProtocolConfig wsConfig = WebSocketServerProtocolConfig.newBuilder()
            .websocketPath("/websocket")
            .maxFramePayloadLength(Integer.MAX_VALUE)
            .checkStartsWith(true).build();
    ch.pipeline().addLast("WebSocketHandler", new WebSocketServerProtocolHandler(wsConfig));
    ch.pipeline().addLast("WebSocketTextHandler", new WebSocketTextHandler());
}
```

**特点：**
- 基于TCP协议，服务端实现较容易；
- 与Http协议兼容，握手阶段采用 80和443端口，因此握手时不易被屏蔽；
- 数据格式较轻量，性能开销小，通信高效；
- 可以发送文本、二进制数据；
- 没有同源限制，客户端可以与任意服务器通信；
- 协议标识符是 ws，加密时是 wss，服务器地址是 URL。

**加密封装**
- Http：TCP——>Http
- Https：TCP——>TLS——>Http
- ws：TCP——>ws
- wss：TCP——>TLS——>ws

### WebSocket的握手
**WebSocket握手流程**
1. 使用 TCP 进行 三次 握手；
2. 客户端向服务端发送一个 Http 请求，服务端接收并相应；
3. 连接建立成功，以后都使用 WebSocket 协议进行数据交互。

> 通过控制 pipeline 对编解码器的增加和删除，来控制 使用 TCP 握手，然后 使用 WebSocket 协议传输数据：初始的 pipeline 中有 http 协议的编解码器，连接建立后，删除了 http 协议的编解码器，加入了 websocket 的编解码器。（动态编解码器）

> 主要源码逻辑： 
> - io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler#handlerAdded；
> - io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandshakeHandler#channelRead
> - io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker#handshake

**[WebSocket协议](https://www.cnblogs.com/chyingp/p/websocket-deep-in.html)**


**WebSocket的握手成功事件**
> 通过对 ctx 添加监听器，监听握手等事件。

```java
@Slf4j
@ChannelHandler.Sharable
public class HandshakeEventHandler extends ChannelInboundHandlerAdapter {
    public static final HandshakeEventHandler INSTANCE = new HandshakeEventHandler();
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            // WebSocket 握手成功事件
            WebSocketServerProtocolHandler.HandshakeComplete event = (WebSocketServerProtocolHandler.HandshakeComplete) evt;
            log.info("握手成功事件 URI={}", event.requestUri());
            log.info("握手成功事件，头部信息={}", event.requestHeaders().toString());
        } else {
            // 其他事件 交由父类 处理
            super.userEventTriggered(ctx, evt);
        }
    }
}
```


# Netty 的组件详解
## Handler 的线程安全问题

### Handler 为什么会有线程安全问题
1. 每一次客户端连接，都 new 一个新的 hander 对象，那么不会有线程安全问题；
2. 此时，每发生一次客户端连接，都会创建一套新的 pipeline 处理器；
3. 如果客户端连接数特别大，内存会生成特别多的 handler，造成性能影响；
4. **为了避免性能影响，可以将 无状态 的 Handler 配置成 单例共享的 Handler，实现 Handler 共享。**

> 无状态：Handler 中没有 其他 成员变量 等。

### 共享 Handler 的线程安全
**自定义的 Handler 中，只要不包含 其他的 成员变量，就可以认为是 无状态 的 Handler，只需要再配置成 单例的，添加 @ChannelHandler.Sharable 注解即可实现 线程安全的 共享Handler**

```java
@Slf4j
@ChannelHandler.Sharable
public class WebSocketTextHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static final WebSocketTextHandler INSTANCE = new WebSocketTextHandler();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        log.debug(msg.text());
    }
}
```
## 心跳事件监听机制
Netty 实现的服务端 和 客户端 之间是 长连接，通过 ping / pong 保持通信，确保连接正常。

### 方式一：客户端定期发送 心跳包(推荐)
**添加心跳监听器**
```java
/*
    readerIdleTimeSeconds：读超时,触发 IdleStateEvent#READER_IDLE 事件
    writerIdleTimeSeconds：写超时,触发 IdleStateEvent#WRITER_IDLE 事件
    allIdleTimeSeconds：读/写超时,触发 IdleStateEvent#ALL_IDLE 事件
 */
ch.pipeline().addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS));
ch.pipeline().addLast("心跳事件监听处理器", new IdleStateEventHandler());
```
**心跳监听事件**
```java
@Slf4j
public class HeartBeatServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventType = null;
            switch (event.state()) {
                case READER_IDLE: eventType = "读空闲"; break;
                case WRITER_IDLE: eventType = "写空闲"; break;
                case ALL_IDLE: eventType = "读写空闲"; break;
                default: eventType = "UN_DEFINED"; break;
            }
            log.warn("[{}]-超时事件:[{}]", ctx.channel().remoteAddress(), eventType);
            // 如果发生空闲，关闭通道
            ctx.channel().close();
        }
    }
}
```

### 方式二：服务端定期发送 心跳包
```java
if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
    // 握手成功后，向 channel 添加 定时任务：10s后启动，每隔10s执行一次 Runnable 任务
    final ScheduledFuture<?> scheduledFuture = ctx.channel().eventLoop().scheduleAtFixedRate(() -> {
        ctx.channel().writeAndFlush(new PingWebSocketFrame());
    }, 10, 10, TimeUnit.SECONDS);

    // 当 连接 关闭时，移除 定时任务
    ctx.channel().closeFuture().addListener(channelFuture -> {
        scheduledFuture.cancel(true);
    });
} else {
    ctx.fireChannelRead(evt);
}
```

# Netty 的一些原理
## PoolChunk 内存块原理
- Page：可分配的最小内存单元，默认为 8192B = 8KB。
- Chunk：Page 的集合，默认是 16777216B = 16M。
- PoolChunk：把一整块内存按照 8192 字节（Page）进行均分，一共分成 2048 个 Page页。
- PoolChunkList：
- PoolSubpage：
- PoolArena：


## Netty 的零拷贝
[Java NIO中，关于DirectBuffer，HeapBuffer的疑问？](https://www.zhihu.com/question/57374068/answer/152691891)

> 实际上，推荐使用 堆外内存。堆外内存也是池化的，不存在频繁地创建与释放的过程。并且在 数据从内核态转向用户态时，可以减少内存拷贝。


### FileRegion#transferTo
Netty 的文件传输 调用 FileRegion#transferTo 方法，可以直接将文件缓冲区的数据发送到目标 Channel，避免通过循环 write 方式导致内存拷贝问题。

### CompositeByteBuf 合并 ByteBuf
可以将多个 ByteBuf 直接合成一个 ByteBuf，避免了在合并过程中的拷贝。
```java
public static void compositeByteBufTest() {
    ByteBuf directBuf = UnpooledByteBufAllocator.DEFAULT.buffer();
    ByteBuf heapBuf = UnpooledByteBufAllocator.DEFAULT.heapBuffer();
    directBuf.writeByte(1);
    heapBuf.writeByte(2);

    CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();
    compositeByteBuf.addComponent(true, directBuf);
    compositeByteBuf.addComponent(true, heapBuf);
    System.out.println(compositeByteBuf.readByte());
    System.out.println(compositeByteBuf.readByte());
    compositeByteBuf.release();

    System.out.println(directBuf.refCnt());
    System.out.println(heapBuf.refCnt());
}
```
### ★ Unpooled#wrappedBuffer 包装ByteBuf
通过 wrap 操作，将 byte[] 数组、ByteBuf、ByteBuffer 等 零拷贝地 包装成一个 Netty ByteBuf 对象，进而避免内存拷贝操作。
```java
public static void main(String[] args) {
    final ByteBuffer byteBuffer = ByteBuffer.allocate(16);
    // 会发生 内存拷贝
    final ByteBuf byteBuf1 = ByteBufAllocator.DEFAULT.buffer(byteBuffer.capacity()).writeBytes(byteBuffer);

    // 内存 零拷贝
    ByteBuf byteBuf2 = Unpooled.wrappedBuffer(byteBuffer);
    
    byteBuf1.release();
    byteBuf2.release();
}
```

### ByteBuf#slice 零拷贝切分
将 ByteBuf 分解为多个 共享同一个 存储区域的 ByteBuf，分解过程中 避免了内存的拷贝。

```java
com.wang.netty.daxin.chapter03utils.ByteBufAdvance
```

# Netty 优化
## Guava#EventBus
> Netty 接收到消息后，可以使用 Guava 工具类中的 EventBus 发布订阅模式，进行数据的异步处理，从而将 EventLoop 线程任务 与 业务线程任务 解耦，加快 EventLoop 线程的响应速度，提高 EventLoop 处理其它IO事件的性能。

```java
@Override
protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    log.info("[ReceiveMsgHandler#channelRead0] 接收到信息：{}", msg);
    AsyncEventBus eventBus = new AsyncEventBus("EventBusName", new AsyncConfig().executorService());
    eventBus.register(new EventSubscribe());
    eventBus.post(msg);
}
//----------------------------------- 业务处理逻辑
@Slf4j
public class EventSubscribe {
    @Subscribe
    @AllowConcurrentEvents
    public void processEvent(String event) {
        log.info("[EventSubscribe#processEvent] 处理业务逻辑：{}", event);
    }
}
//----------------------------------- 线程池配置
@Configuration
public class AsyncConfig {
    @Bean
    public Executor executorService() {
        return new ThreadPoolExecutor(50, 200, 30, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(528),
                new ThreadFactoryBuilder().setNameFormat("EventBus-thread").build());
    }
}
```

## 针对 Linux 平台优化

### EpollEventLoopGroup
> 在 Linux 平台中，做了进一步的优化，性能会比 NioEventLoopGroup 更好一点。

```java
public void startLinux() {
    EventLoopGroup bossGroup = new EpollEventLoopGroup(1);
    EventLoopGroup workGroup = new EpollEventLoopGroup(128);
    try {
        final ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workGroup)
                .channel(EpollServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(NioChannelOption.SO_RCVBUF, 8 * 1024)
                .childOption(NioChannelOption.SO_SNDBUF, 8 * 1024)
                .childOption(NioChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT)
                .childHandler(new NettyServerInitializer(countClientCount));
        int port = nettyConfig.getServerPort();
        serverBootstrap.bind(port).sync().channel().closeFuture().addListener(channelFuture -> {
            log.info("Netty Server stop in gracefully...");
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        });
        log.info("Netty Server start on port: {}", port);
    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    }
}
```

### Netty 使用 SSL 加解密
> 在 Linux 平台中，使用 OpenSSL 代替 JDK自带的 SslEngine。

**优化前，SSL加密使用的是 SslEngine**
```java
@Slf4j
public class SslServerInitializer extends ChannelInitializer {
    private static SslContext sslContext;
    static {
        // 证书、私钥的地址
        File certChainFile = new File("/home/app/nginx.crt");
        File keyFile = new File("/home/app/pkcs8_rsa_private_key.pem");
        try {
            sslContext = SslContextBuilder.forServer(certChainFile, keyFile)
                    .clientAuth(ClientAuth.NONE)
                    .sslProvider(SslProvider.JDK).build();
        } catch (SSLException e) {
            log.error(e.getMessage());
        }
    }
    @Override
    protected void initChannel(Channel ch) throws Exception {
        // SSL 编解码
        ch.pipeline().addLast("ssl", sslContext.newHandler(ByteBufAllocator.DEFAULT));
        // Http 编解码
        ch.pipeline().addLast("http", new HttpServerCodec());
        // Http 协议包收集器
        ch.pipeline().addLast("HttpObjectAggregator", new HttpObjectAggregator(Integer.MAX_VALUE));
        ch.pipeline().addLast("HttpHandler", new HttpHandler());
    }
}
```

**优化后，使用OpenSSL**
```java
// .sslProvider(SslProvider.JDK).build();
.sslProvider(SslProvider.OPENSSL).build();
```

# 集成 Guava#ProtocolBuffers 序列化协议
```java
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        Pipeline pipeline = ch.pipeline();
        // 解码器
        pipeline.addLast("FrameDecoder", new LengthFieldBasedFrameDecoder(1048576,0,4,0,4));
        pipeline.addLast("ProtobufDecoder", new ProtobufDecoder(ImServerProto.ImLogin.getDefaultInstance()));
        // 编码器
        pipeline.addLast("FrameEncoder", new LengthFieldPrepender(4));
        pipeline.addLast("ProtobufEncoder", new ProtobufEncoder());
        // 业务处理器
    }
}
```





