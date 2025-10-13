package com.coffee.project.service;

import com.coffee.project.common.Result;
import com.coffee.project.domain.User;
import com.coffee.project.dto.LoginDTO;
import com.coffee.project.dto.PasswordResetDTO;
import com.coffee.project.dto.RegisterDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 用户服务接口
 * 定义用户相关核心业务操作，包括：
 * 1. 用户注册
 * 2. 用户登录
 * 3. 密码重置
 * 4. 用户信息更新（昵称、头像）
 */
public interface UserService {

    /**
     * 用户注册功能
     * 接收前端传来的注册信息，执行注册流程
     *
     * @param registerDTO 包含用户名、密码、确认密码等注册信息的 DTO
     * @return 返回 Result 对象封装操作结果：
     * - code: 状态码（200 成功，500 失败）
     * - message: 描述信息
     * - data: 可返回额外信息（如新注册用户ID），通常为 null
     */
    Result<Object> register(RegisterDTO registerDTO);

    /**
     * 用户登录功能
     * 验证用户名和密码，返回用户详细信息
     *
     * @param loginDTO 登录请求数据，包含用户名和密码
     * @return 返回 Result 封装操作结果：
     * - 成功：data 中包含对应 User 实体或部分用户信息（如 id、昵称、token）
     * - 失败：返回错误信息或抛出异常
     */
    Result<Object> login(LoginDTO loginDTO);

    /**
     * 密码重置功能
     * 处理用户忘记密码后的密码重置请求，通常需要身份验证
     *
     * @param request 密码重置请求，包含用户名、邮箱、新密码等信息的 DTO
     * @return 返回 Result 封装操作结果：
     * - 成功：密码重置成功
     * - 失败：验证失败、用户不存在等
     */
    Result<Object> passwordReset(PasswordResetDTO request);

    /**
     * 更新用户昵称
     *
     * @param id   用户ID
     * @param name 新昵称
     * @return 返回 Result 封装操作结果：
     * - 成功：昵称更新成功
     * - 失败：用户不存在或昵称不合法
     */
    Result<Object> updateName(Long id, String name);

    /**
     * 更新用户头像
     *
     * @param id   用户ID
     * @param file 上传的头像文件
     * @return 返回 Result 封装操作结果：
     * - 成功：头像更新成功
     * - 失败：文件上传失败、文件类型不合法等
     * @throws IOException 文件操作异常
     */
    Result<Object> updateAvatar(Long id, MultipartFile file) throws IOException;
}
