package com.wang.think.mybatisspring;

/**
 * @description: Spring 整合 MyBatis——Mapper，数据库操作的映射文件
 * @author: wei·man cui
 * @date: 2021/1/19 8:44
 */
public interface UserPoMapper {

    /**
     * 插入
     *
     * @param userPo 数据
     */
    void insertUserPo(UserPo userPo);

    /**
     * 查询
     *
     * @param id id
     * @return userPo
     */
    UserPo getUserPo(Integer id);

}
