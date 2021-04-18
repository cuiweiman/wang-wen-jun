package com.wang.boot.webflux.service.impl;

import com.google.common.base.Preconditions;
import com.wang.boot.webflux.domain.User;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @description:
 * @date: 2021/4/17 19:03
 * @author: wei·man cui
 */
@Slf4j
public class UserServiceImpl {

    private UserCache cache = UserCache.getInstance();

    public User listUser() {
        return cache.getAll().get(0);
    }

    public boolean saveUser(User user) {
        cache.saveUser(user.getId(), user);
        log.info("save user success");
        return true;
    }

    public boolean deleteById(String id) {
        return cache.remove(id);
    }


    public User getUser(String id) {
        return cache.getUser(id);
    }
}

class UserCache {


    private final Map<String, User> cache;

    private UserCache() {
        this.cache = new HashMap<>();
        this.cache.put("123", new User("123", "小红"));
    }

    public void saveUser(String id, User user) {
        Preconditions.checkNotNull(id, "ID不能为空");
        Preconditions.checkNotNull(user, "用户信息不能为空");
        this.cache.put(id, user);
    }


    public User getUser(String id) {
        return this.cache.get(id);
    }


    public List<User> getAll() {
        return this.cache.entrySet()
                .parallelStream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public User getAndRemoveUser(String id) {
        return this.cache.remove(id);
    }


    public boolean remove(String id) {
        return Objects.equals(id, this.cache.remove(id).getId());
    }


    public Integer size() {
        return this.cache.size();
    }

    private enum UserCacheEnum {
        /**
         * 实例
         */
        INSTANCE;
        private final UserCache userCache;

        UserCacheEnum() {
            this.userCache = new UserCache();
        }

        public UserCache getUserCache() {
            return this.userCache;
        }
    }

    public static UserCache getInstance() {
        return UserCacheEnum.INSTANCE.getUserCache();
    }
}