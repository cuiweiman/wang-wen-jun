package com.wang.es.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * @description: es 数据对象
 * @date: 2020/11/22 22:20
 * @author: wei·man cui
 */
@Data
@Document(indexName = "sys_user_index")
public class SysUser implements Serializable {

    private Long id;

    /**
     * 账号
     */
    @Field(type = FieldType.Keyword)
    private String username;

    /**
     * 密码
     */
    @Field(type = FieldType.Keyword)
    private String password;


    /**
     * 昵称
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String nickname;

    /**
     * 邮箱
     */
    @Field(type = FieldType.Keyword)
    private String email;

    /**
     * 状态（0：锁定，1：解锁）
     */
    @Field(type = FieldType.Integer)
    private Integer status;

    /**
     * 创建人
     */
    @Field(type = FieldType.Keyword)
    private String createUser;

    /**
     * 更新人
     */
    @Field(type = FieldType.Keyword)
    private String updateUser;

    /**
     * 年龄
     */
    @Field(type = FieldType.Double)
    private Double age;

}
