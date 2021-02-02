package com.wang.guava.reference;

import java.lang.ref.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @description: Reference示例
 * @author: wei·man cui
 * @date: 2020/8/21 9:29
 */
public class ReferenceExample {

    /**
     * 设置 VM Options：-Xmx128M -Xms64M -XX:+PrintGCDetails
     */
    public static void main(String[] args) throws InterruptedException {
        // strongRef();
        // softReference();
        // weakReference();
        softWeakPhantomRef();
    }

    /**
     * 强引用：即便抛出 OutOfMemory ，强引用也不会被GC
     */
    public static void strongRef() throws InterruptedException {
        int current = 1;
        List<Ref> contianer = new ArrayList<>();
        for (; ; ) {
            contianer.add(new Ref(current));
            current++;
            System.out.println("The  [" + current + "] Ref will be insert into container");
            TimeUnit.MILLISECONDS.sleep(500);
        }
    }

    /**
     * 软引用：new SoftReference<>(new Object()); 创建软引用对象
     * SoftReference 可以检测到JVM内存空间是否足够，不足时就GC掉软引用
     * 因为这个特性，适合用于开发 缓存Cache(适度放大休眠时间，给JVM充足时间执行GC)
     */
    public static void softReference() throws InterruptedException {
        int current = 1;
        List<SoftReference> contianer = new ArrayList<>();
        for (; ; ) {
            contianer.add(new SoftReference<>(new Ref(current)));
            current++;
            System.out.println("The  [" + current + "] Ref will be insert into container");
            TimeUnit.SECONDS.sleep(1);
        }
    }

    /**
     * 弱引用：只要被扫描标记，就会被GC，不论是 Minor GC还是Full GC。
     * <p>
     * 年轻代回收内存：Minor GC；
     * 老年代回收内存：Major GC；
     * 整个堆回收内存：Full GC。
     * </P>
     */
    public static void weakReference() throws InterruptedException {
        int current = 1;
        List<WeakReference<Ref>> contianer = new ArrayList<>();
        for (; ; ) {
            contianer.add(new WeakReference<>(new Ref(current)));
            current++;
            System.out.println("The  [" + current + "] Ref will be insert into container");
            TimeUnit.MILLISECONDS.sleep(200);
        }
    }

    /**
     * 软/弱/虚引用的比较.
     * System.gc()需要时间，因此休眠
     */
    public static void softWeakPhantomRef() throws InterruptedException {
        /*Ref softRef = new Ref(10);
        SoftReference<Ref> soft = new SoftReference<>(softRef);
        softRef = null;
        System.gc();
        TimeUnit.SECONDS.sleep(1);
        System.out.println(softRef);
        System.out.println("【软引用】" + soft.get().index);*/

        /*Ref weakRef = new Ref(8);
        WeakReference<Ref> weak = new WeakReference<>(weakRef);
        weakRef = null;
        System.gc();
        TimeUnit.SECONDS.sleep(1);
        System.out.println(weakRef);
        System.out.println("【弱引用】被GC掉，空指针异常：" + weak.get().index);*/

        ReferenceQueue<Ref> queue = new ReferenceQueue<>();
        PhantomReference<Ref> phantom = new PhantomReference<>(new Ref(5), queue);
        System.out.println("【虚引用】：" + phantom.get());
        System.gc();
        // TimeUnit.SECONDS.sleep(1);
        Reference<? extends Ref> object = queue.remove();
        System.out.println(object);
    }

    /**
     * GC时，会扫描两次。
     * 首次标记：判断是否需要执行finalize()方法，判定是否需要执行的标准——对象是否重写了finalize(),
     * 或者虚拟机已经调用了finalize()方法，若是，则判定为不执行。
     * 第二次标记：虚拟机执行finalize()方法，对象被放置F-Queue队列，若在finalize()方法中 对象被重新引用则存活
     * 否则被标记回收。
     */
    private static class Ref {
        // 1M
        private byte[] data = new byte[1024 * 1024];

        private final int index;

        public Ref(int index) {
            this.index = index;
        }

        @Override
        protected void finalize() throws Throwable {
            System.out.println("The index [" + index + "] will be GC.");
        }
    }

}
