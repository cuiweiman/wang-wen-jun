package com.wang.basic.snowflake;

import lombok.extern.slf4j.Slf4j;

/**
 * 1bit符号位 | 41bit时间戳 | 10bit机器标识 | 12bit自增序列
 * 其中 10bit机器标识 分为 5个机房+5个服务器，一共可以表示 32机房*32服务器/机房= 1024台机器
 *
 * @description: 雪花算法 生成ID
 * @author: cuiweiman
 * @date: 2022/1/23 22:34
 */
@Slf4j
public class SnowFlake {

    /**
     * 服务器ID 位数 及 可以表示的 最大值：5位，最大 32个服务器。
     */
    private static final Long WORK_ID_BITS = 5L;

    /**
     * 机房ID 位数  及 可以表示的 最大值：5位，最大 32个机房/数据中心。
     */
    private static final Long DATA_CENTER_ID_BITS = 5L;

    /**
     * 消息序列 12位，因此 同一级别的时间戳中可以有 2^12=4096条消息序列
     */
    private static final Long SEQUENCE_BITS = 12L;
    private final Long sequenceMax = ~(-1L << SEQUENCE_BITS);

    /**
     * 偏移量： 41bit时间戳 | 5bit机房标识 | 5bit服务器标识 | 12bit自增序列
     */
    private final Long WORKER_SHIFT_BITS = SEQUENCE_BITS;
    private final Long DATA_CENTER_SHIFT_BITS = WORKER_SHIFT_BITS + WORK_ID_BITS;
    private final Long OFFSET_SHIFT_BITS = DATA_CENTER_SHIFT_BITS + DATA_CENTER_ID_BITS;


    /**
     * 初试时间戳偏移量：2010-11-04 09:42:54，可以使用当前时间戳
     */
    private static final Long OFFSET = 1288834974657L;


    private final Long workId;
    private final Long dataCenterId;
    private Long sequence;

    /**
     * 记录 上一个 的时间戳
     */
    private long lastTimestamp = -1L;

    /**
     * 构造函数
     */
    public SnowFlake(Long workId, Long dataCenterId) {
        Long workIdMax = ~(-1L << WORK_ID_BITS);
        if (workId > workIdMax || workId < 0) {
            throw new IllegalArgumentException(
                    String.format("worker Id can't be greater than %d or less than 0", workIdMax));
        }
        Long dataCenterIdMax = ~(-1L << DATA_CENTER_ID_BITS);
        if (dataCenterId > dataCenterIdMax || dataCenterId < 0) {
            throw new IllegalArgumentException(
                    String.format("dataCenter Id can't be greater than %d or less than 0", dataCenterIdMax));
        }
        this.workId = workId;
        this.dataCenterId = dataCenterId;
    }

    public synchronized Long nextId() {
        long timestamp = timeGen();
        // 不允许 时钟回拨，及 当前时间戳必须 大于等于 上一个ID的时间戳
        if (timestamp < lastTimestamp) {
            log.error("clock is moving backwards. Rejecting requests until {}.", lastTimestamp);
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
                            lastTimestamp - timestamp));
        }
        // 同一个时间级别下，使用消息序列号
        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & sequenceMax;
            // 时间序列用完了，获取下一个时间
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        // 每获取一次 ID，同步修改一次 时间戳记录
        lastTimestamp = timestamp;
        return (timestamp - OFFSET) << OFFSET_SHIFT_BITS
                | dataCenterId << DATA_CENTER_SHIFT_BITS
                | workId << WORKER_SHIFT_BITS
                | sequence;
    }

    /**
     * 保证 时间戳 永远大于 记录的上一个 时间戳
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 获取当前时间戳
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }
}