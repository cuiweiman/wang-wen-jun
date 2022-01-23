package com.wang.basic.snowflake;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 时间戳问题博文：https://blog.csdn.net/wanghao112956/article/details/102794497
 * 缓慢的时间戳：http://pzemtsov.github.io/2017/07/23/the-slow-currenttimemillis.html
 * <p>
 * Linux 环境中高并发下 System.currentTimeMillis() 这个 API 对比 Windows 环境有近百倍的性能差距;
 * <p>
 * 起一个线程定时维护一个毫秒时间戳，以覆盖 JDK 的 System.currentTimeMillis(),
 * 虽然这样固然会造成一定的时间精度问题，但是能换来的百倍的性能提升。
 *
 * @description:
 * @author: cuiweiman
 * @date: 2022/1/23 23:22
 */
public class SystemClock {

    private final long period;
    private final AtomicLong now;

    private SystemClock(long period) {
        this.period = period;
        // 时间戳缓存在 AtomicLong 中，并定时更新
        this.now = new AtomicLong(System.currentTimeMillis());
        scheduleClockUpdating();
    }

    /**
     * 获取当前毫秒时间戳
     */
    public static long now() {
        return getInstance().now.get();
    }

    /**
     * 起一个线程定时刷新时间戳
     */
    private void scheduleClockUpdating() {
        ScheduledThreadPoolExecutor systemClockScheduler = new ScheduledThreadPoolExecutor(1, runnable -> {
            Thread thread = new Thread(runnable, "SYSTEM-CLOCK");
            thread.setDaemon(true);
            return thread;
        });
        systemClockScheduler.scheduleAtFixedRate(() ->
                now.set(System.currentTimeMillis()), period, period, TimeUnit.MILLISECONDS);
    }

    private enum SystemClockEnum {
        /**
         * 系统时钟
         */
        SYSTEM_CLOCK;
        private final SystemClock systemClock;

        SystemClockEnum() {
            systemClock = new SystemClock(1);
        }

        public SystemClock getInstance() {
            return systemClock;
        }
    }

    private static SystemClock getInstance() {
        return SystemClockEnum.SYSTEM_CLOCK.getInstance();
    }

}
