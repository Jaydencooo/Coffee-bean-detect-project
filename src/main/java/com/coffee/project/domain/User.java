package com.coffee.project.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 用户实体类（Entity），用于映射数据库中的用户表。
 * <p>
 * 该类使用了 MyBatis-Plus 的注解来实现与数据库表的映射，并且通过 Lombok 的 @Data 注解自动生成 getter 和 setter 方法，
 * 以及 toString 方法，从而简化了代码的编写。
 * </p>
 */
@Data
@TableName("user") // 指定该类映射的数据库表名为 "user"
public class User {
    /**
     * 用户的唯一标识符，通常是一个自增的长整型数字。
     * <p>
     * 在数据库中，该字段通常被设置为主键，用于唯一标识每个用户记录。
     * </p>
     */
    @TableId(type = IdType.AUTO) // 指定该字段为表的主键，并且采用自增策略
    private Long id;

    /**
     * 用户名，用于用户登录和识别。
     * <p>
     * 该字段应具有唯一性，以确保每个用户都有一个独特的用户名。在用户注册时，需要对用户名进行唯一性校验。
     * </p>
     */
    private String username;

    /**
     * 用户密码，用于用户身份验证。
     * <p>
     * 密码应进行加密存储，以保护用户的安全。在用户注册或修改密码时，应使用加密算法（如 bcrypt）对密码进行加密。
     * </p>
     */
    private String password;

    /**
     * 用户的电子邮件地址。
     * <p>
     * 电子邮件地址可用于用户找回密码或接收通知。在用户注册时，应验证电子邮件地址的格式是否正确。
     * </p>
     */
    private String email;

    /**
     * 用户的电话号码。
     * <p>
     * 电话号码可用于用户身份验证或接收短信通知。在用户注册时，应验证电话号码的格式是否正确。
     * </p>
     */
    private String phone;

    /**
     * 用户创建时间，记录用户账户创建的时间点。
     * <p>
     * 该字段通常在用户注册时自动生成，用于记录用户账户的创建时间。
     * </p>
     */
    private Date createTime;

    /**
     * 用户的头像 URL。
     * <p>
     * 该字段存储用户头像的图片地址，可用于在用户界面中显示用户头像。
     * </p>
     */
    private String avatarUrl;

    /**
     * 用户的真实姓名。
     * <p>
     * 该字段存储用户的真实姓名，可用于在用户界面中显示用户的真实姓名。
     * </p>
     */
    private String name;
}