package com.coffee.project.dto;

import lombok.Data;

/**
 * LoginDTO 是登录接口用来接收前端传来的登录数据的对象
 *
 * DTO（Data Transfer Object）就是数据传输对象，专门用来在不同层之间传递数据的类
 * 和数据库实体类不同，DTO 只关注接口需要的字段，通常只包含属性，没有业务逻辑
 *
 * 这里 LoginDTO 用于接收用户登录时提交的用户名和密码
 */
@Data  // Lombok 注解，自动生成 getter、setter、toString 等方法
public class LoginDTO {
    /**
     * 用户名，前端登录时填写的账号名
     */
    private String username;

    /**
     * 密码，前端登录时填写的密码
     */
    private String password;
}
