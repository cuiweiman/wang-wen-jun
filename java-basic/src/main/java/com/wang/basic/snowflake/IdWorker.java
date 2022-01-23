package com.wang.basic.snowflake;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2022/1/23 23:15
 */
public class IdWorker {

    private IdWorker() {
    }

    private enum Singleton {
        /**
         * 单例
         */
        INSTANCE;
        private final SnowFlake snowFlake;

        Singleton() {
            this.snowFlake = new SnowFlake(1L, 1L);
        }

        public SnowFlake getInstance() {
            return this.snowFlake;
        }
    }

    private static SnowFlake getInstance() {
        return Singleton.INSTANCE.getInstance();
    }

    public static Long nextId() {
        return IdWorker.getInstance().nextId();
    }

}
