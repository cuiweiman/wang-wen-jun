package com.cui.creator.singleton;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 枚举单例
 * @author: wei·man cui
 * @date: 2021/3/3 15:35
 */
public class BusinessChannelCache {

    private final Map<String, String> deviceChannelCache;

    private BusinessChannelCache() {
        deviceChannelCache = new ConcurrentHashMap();
    }

    private enum BusinessChannelCacheInnerEnum {
        /**
         * 实例
         */
        INSTANCE;

        private final BusinessChannelCache businessChannelCache;

        BusinessChannelCacheInnerEnum() {
            this.businessChannelCache = new BusinessChannelCache();
        }

        public BusinessChannelCache getBusinessChannelCache() {
            return businessChannelCache;
        }
    }

    public static BusinessChannelCache getInstance() {
        return BusinessChannelCacheInnerEnum.INSTANCE.getBusinessChannelCache();
    }

    /**
     * 存储 设备通道
     *
     * @param deviceId 设备ID
     * @param channel  通道
     */
    public void putDeviceChannel(String deviceId, String channel) {
        this.deviceChannelCache.putIfAbsent(deviceId, channel);
    }

    /**
     * 获取 设备通道缓存
     *
     * @return 不可变容器
     */
    public Map<String, String> getDeviceChannelCache() {
        // return ImmutableMap.copyOf(deviceChannelCache);
        return this.deviceChannelCache;
    }

    /**
     * 删除设备通道
     *
     * @param deviceId 设备ID
     * @return 删除的通道
     */
    public String removeDeviceChannel(String deviceId) {
        return this.deviceChannelCache.remove(deviceId);
    }

    public Integer size() {
        return this.deviceChannelCache.size();
    }

}

