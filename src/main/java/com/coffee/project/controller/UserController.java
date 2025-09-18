package com.coffee.project.controller;

import com.coffee.project.common.Result;
import com.coffee.project.domain.User;
import com.coffee.project.dto.LoginDTO;
import com.coffee.project.dto.PasswordResetDTO;
import com.coffee.project.dto.RegisterDTO;
import com.coffee.project.service.UserService;
import com.coffee.project.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * UserController 是用户相关接口的控制器类
 * 负责接收和处理来自前端的用户请求，比如注册、登录和密码重置
 *
 * 控制器类是 Spring MVC 中处理 HTTP 请求的组件
 * 它把请求映射到具体的方法，并调用对应的业务逻辑层服务完成任务
 */
@CrossOrigin(
        origins = "http://localhost:5174",  // 允许来自这个地址的前端请求，解决跨域问题
        allowCredentials = "true",          // 允许请求带上 Cookie 等凭证信息
        allowedHeaders = "*"                // 允许所有请求头
)
@RestController  // 该类是 REST 风格控制器，方法返回的数据会自动转换成 JSON 格式响应给前端
@RequestMapping("/user")  // 这个控制器处理的所有请求都以 /user 开头
public class UserController {

    /**
     * 自动注入 UserService 对象
     * UserService 负责用户相关的业务逻辑，比如操作数据库
     * 控制器只负责接收请求和返回响应，不写业务逻辑
     */
    @Autowired
    private UserService userService;

    /**
     * 用户注册接口
     * 接收前端发送过来的用户注册信息（用户名、密码等）
     * @param request 前端以 JSON 格式传来的注册信息，会被自动转换成 RegisterDTO 对象
     * @return 返回一个统一格式的结果，成功时提示“注册成功”
     */
    //@RequestBody 表示该方法从 HTTP 请求的请求体里接收数据（JSON格式），并自动转换成 RegisterDTO 对象。
    //RegisterDTO 是一个数据传输对象，封装了注册时前端提交的用户名、密码、邮箱等字段。
    @PostMapping("/register")  // 处理 POST 请求 /user/register
    public Result<?> register(@Valid @RequestBody RegisterDTO request) {

        return userService.register(request);
    }

    /**
     * 用户登录接口
     * 接收前端发送的用户名和密码，进行身份验证
     * @param request 登录请求数据，自动转换成 LoginDTO 对象
     * @return
     * 返回登录成功的用户信息，失败会在服务层抛异常（这里简化没写异常处理）
     */
    @PostMapping("/login")  // 处理 POST 请求 /user/login
    public Result<?> login(@RequestBody LoginDTO request) {

        return userService.login(request);
    }


    /**
     * 密码重置接口
     * 接收前端传来的旧密码、新密码等信息，完成密码修改
     * @param request 密码重置请求数据，转换成 PasswordResetDTO 对象
     * @return 返回密码重置成功的提示信息
     */
    @PostMapping("/reset-password")  // 处理 POST 请求 /user/reset-password
    public Result<?> passwordReset(@RequestBody PasswordResetDTO request) {
        // 调用 UserService 重置密码方法，更新用户密码
        return userService.passwordReset(request);
    }
}
