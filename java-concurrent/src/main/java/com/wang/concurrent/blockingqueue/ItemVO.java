package com.wang.concurrent.blockingqueue;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 使用 DelayQueue 队列，模拟实现 订单延时支付 的逻辑
 * 存放队列的元素，需要 实现 Delayed接口。
 */
public class ItemVO<T> implements Delayed {

    // 延时时长,单位 毫秒，队列中的是纳秒
    private long activeTime;

    private T data;

    public ItemVO(long activeTime, T data) {
        // 将传入的时长，转变单位
        //计算出 从现在开始，延时到什么时候。
        // Delayed接口中的 activeTime 属性，指的是 到期时间，单位纳秒，而ItemVO类中的activeTime，是延时的时长
        this.activeTime = TimeUnit.NANOSECONDS.convert(activeTime, TimeUnit.MILLISECONDS)
                + System.nanoTime();
        this.data = data;
    }

    public long getActiveTime() {
        return activeTime;
    }

    public T getData() {
        return data;
    }


    // 返回元素的 剩余时间
    @Override
    public long getDelay(TimeUnit unit) {
        long d = unit.convert(this.activeTime - System.nanoTime(), TimeUnit.NANOSECONDS);
        return d;
    }

    // 自定义排序：按照 剩余的时间 进行排序
    // 当前 元素的过期时间 - 参数元素的过期时间
    @Override
    public int compareTo(Delayed o) {
        long d = getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
        return (d == 0) ? 0 : (d > 0 ? 1 : -1);
    }
}
