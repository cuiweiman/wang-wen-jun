package com.wang.think.mybatisonly;

import com.wang.think.springjdbc.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

/**
 * @description: MyBatis的单独使用——测试Demo
 * @author: wei·man cui
 * @date: 2021/1/19 9:14
 */
public class TestMapper {

    static SqlSessionFactory sqlSessionFactory;

    static {
        sqlSessionFactory = MyBatisUtil.getSqlSessionfactory();
    }

    @Test
    public void testAdd() {
        try (final SqlSession sqlSession = sqlSessionFactory.openSession()) {
            UserPo userPo = new UserPo("jack", 10, "男");
            final UserPoMapper mapper = sqlSession.getMapper(UserPoMapper.class);
            mapper.insertUserPo(userPo);
            // 这里一定要提交，不然数据不会被存储数据库中
            sqlSession.commit();
        }
    }

    @Test
    public void testGetUserPo() {
        try (final SqlSession sqlSession = sqlSessionFactory.openSession()) {
            Integer id = 4;
            final UserPoMapper mapper = sqlSession.getMapper(UserPoMapper.class);
            final UserPo userPo = mapper.getUserPo(id);
            System.out.println(userPo.toString());
        }
    }

}
