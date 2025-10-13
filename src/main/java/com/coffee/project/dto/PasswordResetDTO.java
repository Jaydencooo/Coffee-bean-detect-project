package com.coffee.project.dto;

import lombok.Data;

/**
 * PasswordResetDTO 是用于找回密码（重置密码）时，接收前端提交的数据对象
 * 当用户忘记密码，需要通过用户名和邮箱验证身份后设置新密码时，会用到这个对象
 * 这个 DTO 只包含找回密码接口需要的字段
 */
@Data  // 自动生成 getter、setter、toString 等方法
public class PasswordResetDTO {

    /**
     * 用户名，用于标识是哪位用户需要重置密码
     */
    private String username;

    /**
     * 新密码，用户希望设置的新密码
     */
    private String newPassword;

    /**
     * 绑定的邮箱，用于验证用户身份，确保是本人操作
     */
    private String email;
}
