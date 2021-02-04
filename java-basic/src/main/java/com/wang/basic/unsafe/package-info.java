/**
 * {@link sun.misc.Unsafe} 不属于Java基础类库，但是被广泛使用的高性能开发库都是基于Unsafe类开发的，比如Netty、Cassandra、Hadoop、Kafka等。
 * 可以提升 Java运行效率，增强 Java 语言底层操作能力。
 * <p>
 * Unsafe 类提供了以下功能:
 * 1. 内存管理：{@link sun.misc.Unsafe#allocateMemory(long)}分配内存，reallocateMemory重新分配内存，copyMemory拷贝内存，freeMemory释放内存，getAddress获取内存地址，
 * addressSize，pageSize，getInt获取内存地址指向的整数，getIntVolatile获取内存地址指向的整数并支持volatile、putInt将整数写入指定内存地址等。
 * Demo：{@link com.wang.basic.unsafe.UnsafeHeapArray}
 * <p>
 * 2. 非常规的对象实例化：allocateInstance 方法提供了另一种创建实例的途径，无需调用构造方法和其他初始化方法，直接生成实例。
 * Demo：{@link com.wang.basic.unsafe.UnsafeObject}
 * <p>
 * 3. 操作类、对象、变量：staticFieldOffset静态域偏移，defineClass定义类，defineAnonymousClass定义匿名类，
 * ensureClassInitialized确保类初始化，objectFieldOffset对象域内存中的偏移等。可以获取到对象的指针，修改指针指向的数据，找到被JVM认定为可回收的对象等。
 * Demo：{@link com.wang.basic.unsafe.UnsafeObject}
 * <p>
 * 4. 数组操作：arrayBaseOffset 数组第一个元素的偏移地址，arrayIndexScale 获取数组中元素的增量地址等方法。可以定位数组元素在内存中的位置。
 * Demo：{@link com.wang.basic.unsafe.UnsafeObject}
 * <p>
 * 5. 多线程同步，包括锁机制和CAS操作等：compareAndSwapInt，compareAndSwap等方法。为Java的锁机制提供了新的解决方法，如AtomicInteger等就是通过本类实现的。
 * compareAndSwap 方法是原子的，可以避免繁重的锁机制。
 * Demo：{@link com.wang.basic.unsafe.UnsafeObject}
 * <p>
 * 6. 挂起和恢复：park、{@link sun.misc.Unsafe#unpark(Object)} 等方法。
 * <p>
 * 7. 内存屏障：包括 loadFence、storeFence、fullFence等方法，是Java8引入的用于定义内存屏障，避免代码重排序。
 * loadFence() 表示该方法之前的所有load操作在内存屏障之前完成。同理storeFence()表示该方法之前的所有store操作在内存屏障之前完成。fullFence()表示该方法之前的所有load、store操作在内存屏障之前完成。
 *
 * @author: wei·man cui
 * @date: 2021/2/4 14:08
 */
package com.wang.basic.unsafe;