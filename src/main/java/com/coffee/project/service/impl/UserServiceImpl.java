package com.coffee.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coffee.project.common.Result;
import com.coffee.project.domain.User;
import com.coffee.project.dto.LoginDTO;
import com.coffee.project.dto.LoginResult;
import com.coffee.project.dto.PasswordResetDTO;
import com.coffee.project.dto.RegisterDTO;
import com.coffee.project.mapper.UserMapper;
import com.coffee.project.service.UserService;
import com.coffee.project.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 用户服务实现类
 *
 * 功能：
 * 1. 用户注册：校验用户名唯一性并创建新用户（密码加密）
 * 2. 用户登录：验证用户凭证，返回用户信息及 JWT
 * 3. 密码重置：验证用户身份后更新密码
 *
 * 安全性：
 * - 密码使用 BCrypt 加密
 * - 登录成功生成 JWT
 * - 统一返回 Result 对象
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户注册
     *
     * @param registerDTO 前端传入注册信息，包括用户名、密码和确认密码
     * @return Result<Object> 注册结果，成功或失败及提示信息
     */
    @Override
    public Result<Object> register(RegisterDTO registerDTO) {
        log.info("注册请求 -> 用户名: {}, 邮箱: {}", registerDTO.getUsername(), registerDTO.getEmail());

        // 1. 检查用户名和邮箱是否已存在（一次查库）
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("username", registerDTO.getUsername())
                .or()
                .eq("email", registerDTO.getEmail());
        User existing = userMapper.selectOne(query);

        if (existing != null) {
            if (existing.getUsername().equals(registerDTO.getUsername())) {
                log.warn("注册失败：用户名已存在 -> {}", registerDTO.getUsername());
                return Result.error("用户名已存在");
            }
            if (existing.getEmail().equals(registerDTO.getEmail())) {
                log.warn("注册失败：邮箱已被注册 -> {}", registerDTO.getEmail());
                return Result.error("邮箱已被注册");
            }
        }

        // 2. 检查两次密码是否一致
        if (!Objects.equals(registerDTO.getPassword(), registerDTO.getConfirmPassword())) {
            log.warn("注册失败：密码不一致 -> 用户名: {}", registerDTO.getUsername());
            return Result.error("输入密码和确认密码不同，请重新输入");
        }

        // 3. 创建新用户并加密密码
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(BCrypt.hashpw(registerDTO.getPassword(), BCrypt.gensalt()));

        // 4. 插入数据库
        if (userMapper.insert(user) <= 0) {
            log.error("注册失败：数据库插入错误 -> 用户名: {}", registerDTO.getUsername());
            return Result.error("注册失败，请重试");
        }

        log.info("注册成功 -> 用户名: {}, 邮箱: {}", user.getUsername(), user.getEmail());
        return Result.success("注册成功");
    }

    /**
     * 用户登录
     *
     * @param loginDTO 前端传入登录信息，包括用户名和密码
     * @return Result<Object> 登录结果，包括用户信息和 JWT
     */
    @Override
    public Result<Object> login(LoginDTO loginDTO) {
        log.info("用户登录尝试，用户名: {}", loginDTO.getUsername());

        // 1. 查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginDTO.getUsername());
        User user = userMapper.selectOne(queryWrapper);

        // 2. 用户名不存在
        if (user == null) {
            log.info("登录失败，用户名不存在: {}", loginDTO.getUsername());
            return Result.error("用户名或密码错误");
        }

        // 3. 校验密码
        if (!BCrypt.checkpw(loginDTO.getPassword(), user.getPassword())) {
            log.info("登录失败，密码错误，用户名: {}", loginDTO.getUsername());
            return Result.error("用户名或密码错误");
        }

        // 4. 生成 JWT
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        String jwt = JwtUtils.generateJwt(claims);

        log.info("登录成功，用户名: {}, 生成 JWT: {}", loginDTO.getUsername(), jwt);

        // 5. 返回结果，包括用户信息和 token
        LoginResult loginResult = new LoginResult();
        loginResult.setToken(jwt);
        loginResult.setUser(user);

        return Result.success(loginResult);
    }

    /**
     * 密码重置
     *
     * @param passwordResetDTO 前端传入密码重置信息，包括用户名、邮箱和新密码
     * @return Result<Object> 重置结果，成功或失败及提示信息
     */
    /**
     * 密码重置
     * <p>
     * 功能：
     * 1. 验证用户名和邮箱是否匹配
     * 2. 对新密码进行加密
     * 3. 更新数据库中的密码
     * 4. 返回操作结果
     *
     * @param passwordResetDTO 包含用户名、邮箱和新密码
     * @return Result<Object> 操作结果
     */
    @Override
    public Result<Object> passwordReset(PasswordResetDTO passwordResetDTO) {
        log.info("密码重置请求开始，用户名: {}", passwordResetDTO.getUsername());

        // 1. 验证用户名和邮箱是否匹配
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", passwordResetDTO.getUsername())
                .eq("email", passwordResetDTO.getEmail());
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            log.warn("密码重置失败：用户名或邮箱不匹配，用户名: {}", passwordResetDTO.getUsername());
            return Result.error("用户名或邮箱不匹配");
        }

        // 2. 检查新密码是否为空
        if (passwordResetDTO.getNewPassword() == null || passwordResetDTO.getNewPassword().isEmpty()) {
            log.warn("密码重置失败：新密码为空，用户名: {}", passwordResetDTO.getUsername());
            return Result.error("新密码不能为空");
        }

        if(BCrypt.checkpw(passwordResetDTO.getNewPassword(), user.getPassword())){
            log.warn("密码重置失败：不能与旧密码相同，用户名: {}", passwordResetDTO.getUsername());
            return Result.error("新密码不能与旧密码相同");
        }



        // 3. 对新密码进行 BCrypt 加密
        String hashedPassword = BCrypt.hashpw(passwordResetDTO.getNewPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        // 4. 更新数据库
        int rows = userMapper.updateById(user);
        if (rows <= 0) {
            log.error("密码重置失败：数据库更新失败，用户名: {}", passwordResetDTO.getUsername());
            return Result.error("密码重置失败，请重试");
        }

        log.info("密码重置成功，用户名: {}", passwordResetDTO.getUsername());
        return Result.success("密码重置成功");
    }

}
