package com.wang.alipay.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * @description: ID生成器
 * @author: wei·man cui
 * @date: 2021/1/4 9:42
 */
public class IdGenerator {

    private static final Snowflake SNOW_FLAKE = IdUtil.getSnowflake(1, 1);

    public static String nextIdStr() {
        return SNOW_FLAKE.nextIdStr();
    }

    public static String seriesNum() {
        return DateUtil.format(DateUtil.getNow(), DateUtil.YMDHMS).concat(nextIdStr());
    }

}
