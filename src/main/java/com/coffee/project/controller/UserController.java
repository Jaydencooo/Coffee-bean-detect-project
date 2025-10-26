package com.coffee.project.controller;

import com.coffee.project.common.Result;
import com.coffee.project.dto.LoginDTO;
import com.coffee.project.dto.PasswordResetDTO;
import com.coffee.project.dto.RegisterDTO;
import com.coffee.project.service.UserService;
import com.coffee.project.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.io.IOException;

/**
 * UserController 是用户相关接口的控制器类
 *
 * 主要功能：
 * 1. 接收前端 HTTP 请求（比如注册、登录、修改昵称、上传头像、重置密码）
 * 2. 调用 UserService 完成业务逻辑（如操作数据库）
 * 3. 返回统一的 Result 对象给前端
 *
 * @Slf4j: 提供日志功能，可以使用 log.info/debug/error 输出日志
 * @RestController: 表示这是一个 REST 风格的控制器，返回 JSON 数据
 * @RequestMapping("/user"): 所有接口的前缀都是 /user
 */
@Slf4j
@CrossOrigin(
        origins =  {"http://localhost:8091", "http://localhost:8092"},// 允许前端项目地址发起跨域请求
        allowCredentials = "true",          // 允许携带 Cookie 等凭证信息
        allowedHeaders = "*",                // 允许所有请求头
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS}
)
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 自动注入 UserService
     * UserService 负责所有用户相关的业务逻辑：
     *  - 注册
     *  - 登录
     *  - 修改昵称
     *  - 上传头像
     *  - 重置密码
     * 控制器只负责接收请求和返回响应，不直接操作数据库
     */
    @Autowired
    private UserService userService;

    /**
     * 修改用户昵称接口
     *
     * @param name 前端传来的新昵称
     * @param token HTTP 请求头里的 Authorization（JWT token）
     * @return Result 对象（成功或失败）
     *
     * 流程：
     * 1. 检查昵称是否为空
     * 2. 检查 token 是否存在
     * 3. 通过 JwtUtils 从 token 中解析用户 id
     * 4. 调用 Service 层方法更新昵称
     */
    @PostMapping("/update/name")
    public Result<Object> updateName(@RequestParam("name") String name,
                                     @RequestHeader("Authorization") String token) {

        if (name == null || name.trim().isEmpty()) {
            return Result.error("昵称不能为空");
        }

        if (token == null || token.isEmpty()) {
            return Result.error("未登录或 token 无效");
        }

        // 从 JWT token 获取用户 id
        Long id = JwtUtils.getUserId(token);
        if (id == null) {
            return Result.error("未登录或 token 无效");
        }

        // 调用 Service 层更新昵称并返回结果
        return userService.updateName(id, name);
    }

    /**
     * 上传/修改用户头像接口
     *
     * @param file 前端上传的头像文件（MultipartFile）
     * @param token HTTP 请求头里的 Authorization（JWT token）
     * @return Result 对象（成功或失败）
     *
     * 流程：
     * 1. 检查文件是否为空
     * 2. 检查 token 是否存在
     * 3. 解析 token 获取用户 id
     * 4. 调用 Service 层更新头像（会处理文件存储逻辑）
     */
    @PostMapping("/update/avatar")
    public Result<?> updateAvatar(@RequestParam("file") MultipartFile file,
                                  @RequestHeader("Authorization") String token) throws IOException {

        if(file == null || file.isEmpty()){
            return Result.error("上传图片不能为空");
        }

        if (token == null || token.isEmpty()) {
            return Result.error("未登录或 token 无效");
        }

        // 从 JWT token 获取用户 id
        Long id = JwtUtils.getUserId(token);
        if (id == null) {
            return Result.error("未登录或 token 无效");
        }

        // 调用 Service 层更新头像
        return userService.updateAvatar(id, file);
    }

    /**
     * 用户注册接口
     *
     * @param request 前端传来的注册信息（JSON格式），会被自动转换为 RegisterDTO 对象
     * @return Result 对象（成功或失败）
     *
     * 流程：
     * 1. 前端以 JSON 方式提交用户名、密码、邮箱等信息
     * 2. @Valid 注解会自动校验 DTO 上的注解，比如非空、格式
     * 3. 调用 Service 层注册方法
     */
    @PostMapping("/register")
    public Result<?> register(@Valid @RequestBody RegisterDTO request) {
        return userService.register(request);
    }

    /**
     * 用户登录接口
     *
     * @param request 前端传来的登录信息（用户名 + 密码），自动转换为 LoginDTO 对象
     * @return Result 对象，成功返回用户信息和 JWT，失败返回错误信息
     *
     * 流程：
     * 1. 接收前端用户名和密码
     * 2. 调用 Service 层进行身份验证
     * 3. Service 层返回 Result 对象
     */
    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginDTO request) {
        return userService.login(request);
    }

    /**
     * 密码重置接口
     *
     * @param request 前端传来的旧密码、新密码等信息，转换为 PasswordResetDTO 对象
     * @return Result 对象，成功返回提示信息
     *
     * 流程：
     * 1. 接收前端请求数据
     * 2. 调用 Service 层更新用户密码
     * 3. 返回统一格式的结果
     */
    @PostMapping("/reset-password")
    public Result<?> passwordReset(@RequestBody PasswordResetDTO request) {
        return userService.passwordReset(request);
    }
}
