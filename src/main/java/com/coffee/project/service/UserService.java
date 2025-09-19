package com.coffee.project.service;

import com.coffee.project.common.Result;
import com.coffee.project.domain.User;
import com.coffee.project.dto.LoginDTO;
import com.coffee.project.dto.PasswordResetDTO;
import com.coffee.project.dto.RegisterDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 用户服务接口，定义了用户相关的核心业务操作。
 * 包括注册、登录和密码重置等功能。
 */
public interface UserService {

    /**
     * 用户注册功能。
     * 接收前端传来的注册信息，执行注册流程。
     *
     * @param registerDTO 包含用户名、密码、确认密码等注册所需信息的 DTO
     * @return 注册是否成功：
     * - true: 注册成功
     * - false: 注册失败（如用户名已存在、数据格式不正确等）
     */
    Result<Object> register(RegisterDTO registerDTO);

    /**
     * 用户登录功能。
     * 验证用户名和密码，返回用户详细信息。
     *
     * @param loginDTO 登录请求数据，包含用户名和密码
     * @return 登录成功时返回对应的 User 实体，
     *         失败时返回 null 或抛出异常（具体实现而定）
     */
    Result<Object> login(LoginDTO loginDTO);

    /**
     * 密码重置功能。
     * 处理用户忘记密码后的密码重置请求，通常需要身份验证。
     *
     * @param request 密码重置请求，包含用户名、邮箱、新密码等信息
     * @return 密码重置是否成功：
     *         - true: 重置成功
     *         - false: 重置失败（如验证失败、用户不存在等）
     */
    Result<Object> passwordReset(PasswordResetDTO request);

    Result<Object> updateName(Long id,String name);

    Result<Object> updateAvatar(Long id,MultipartFile file) throws IOException;
}
