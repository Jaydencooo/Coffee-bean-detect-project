package com.coffee.project.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * RegisterDTO 是用户注册时，前端提交给后端的数据对象
 * 只包含注册所需的基本字段，如用户名、密码及确认密码
 *
 * DTO（Data Transfer Object）是用于数据传输的简单对象，
 * 通常没有业务逻辑，仅用来接收或返回数据
 */
@Data  // 自动生成所有属性的 getter/setter、toString、equals 等方法
public class RegisterDTO {

    /**
     * 用户名，注册时用户填写的账号名称
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码，注册时用户设置的密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 确认密码，注册时用户再次输入的密码，用于前端或后端校验两次密码是否一致
     */
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
}
