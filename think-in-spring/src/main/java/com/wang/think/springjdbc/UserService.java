package com.wang.think.springjdbc;

import java.util.List;

/**
 * @description: 数据操作接口
 * @author: wei·man cui
 * @date: 2021/1/15 17:13
 */
public interface UserService {

    void save(User user);

    List<User> listUser();

}
