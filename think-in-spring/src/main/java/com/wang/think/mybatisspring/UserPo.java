package com.wang.think.mybatisspring;

/**
 * @description: Spring 整合 MyBatis——PO，数据库映射类
 * @author: wei·man cui
 * @date: 2021/1/19 8:41
 */
public class UserPo {

    private Integer id;

    private String username;

    private Integer age;

    private String sex;

    public UserPo(String username, Integer age, String sex) {
        super();
        this.username = username;
        this.age = age;
        this.sex = sex;
    }

    /**
     * 必须要有此 无参构造函数，不然根据 UserPoMapper.xml中的配置，在查询数据库时，不能通过 反射 构造出 User 实例。
     */
    public UserPo() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "UserPo{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                '}';
    }
}
