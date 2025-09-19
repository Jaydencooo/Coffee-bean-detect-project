package com.coffee.project.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 该类定义了用户实体（Entity），用于映射数据库中的用户表。
 * 此类使用 Lombok 的 @Data 注解自动生成 getter 和 setter 方法，以及 toString 方法。
 */
@Data
@TableName("user")
public class User {
    /**
     * 用户的唯一标识符，通常是一个自增的长整型数字。
     */
    private Long id;

    /**
     * 用户名，用于用户登录和识别。
     * 该字段应具有唯一性，以确保每个用户都有一个独特的用户名。
     */
    private String username;

    /**
     * 用户密码，用于用户身份验证。
     * 密码应进行加密存储，以保护用户的安全。
     */
    private String password;

    /**
     * 用户的电子邮件地址。
     * 电子邮件地址可用于用户找回密码或接收通知。
     */
    private String email;

    /**
     * 用户的电话号码。
     * 电话号码可用于用户身份验证或接收短信通知。
     */
    private String phone;

    /**
     * 用户创建时间，记录用户账户创建的时间点。
     * 该字段通常在用户注册时自动生成。
     */
    private Date createTime;

    private String avatarUrl;

    private String name;

}