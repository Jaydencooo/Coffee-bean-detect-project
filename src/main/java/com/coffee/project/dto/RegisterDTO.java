package com.coffee.project.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 注册数据传输对象（RegisterDTO），用于用户注册时从前端提交到后端的数据。
 * <p>
 * 该类仅包含注册所需的基本字段，如用户名、密码及确认密码，以及电子邮件地址。
 * 它是一个简单的数据传输对象（DTO），通常没有业务逻辑，仅用来接收或返回数据。
 * </p>
 * <p>
 * 该类使用了 Lombok 的 @Data 注解来自动生成 getter 和 setter 方法，以及 toString、equals 和 hashCode 方法，
 * 从而简化了代码的编写。
 * </p>
 * <p>
 * 此外，该类还使用了 JSR 303/JSR 380 标准的注解（如 @NotBlank 和 @Email）来进行数据校验，
 * 确保提交的数据符合业务规则。
 * </p>
 */
@Data  // 自动生成所有属性的 getter/setter、toString、equals 等方法
public class RegisterDTO {

    /**
     * 用户名，注册时用户填写的账号名称。
     * <p>
     * 用户名是用户登录系统时的唯一标识，必须唯一且不能为空。
     * </p>
     */
    @NotBlank(message = "用户名不能为空")  // 确保用户名不为空
    private String username;

    /**
     * 密码，注册时用户设置的密码。
     * <p>
     * 密码是用户登录时用于身份验证的凭证，必须不能为空。
     * </p>
     */
    @NotBlank(message = "密码不能为空")  // 确保密码不为空
    private String password;

    /**
     * 确认密码，注册时用户再次输入的密码，用于前端或后端校验两次密码是否一致。
     * <p>
     * 确认密码字段用于确保用户在注册时两次输入的密码一致，增强密码输入的准确性。
     * </p>
     */
    @NotBlank(message = "确认密码不能为空")  // 确保确认密码不为空
    private String confirmPassword;

    /**
     * 用户的电子邮件地址。
     * <p>
     * 电子邮件地址可用于用户找回密码或接收通知，必须符合电子邮件格式且不能为空。
     * </p>
     */
    @NotBlank(message = "邮箱不能为空")  // 确保电子邮件不为空
    @Email(message = "邮箱格式不正确")  // 确保电子邮件格式正确
    private String email;
}