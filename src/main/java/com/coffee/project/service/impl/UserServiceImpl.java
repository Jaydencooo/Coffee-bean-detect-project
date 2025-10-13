package com.coffee.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coffee.project.common.Result;
import com.coffee.project.domain.User;
import com.coffee.project.dto.LoginDTO;
import com.coffee.project.dto.LoginResultDTO;
import com.coffee.project.dto.PasswordResetDTO;
import com.coffee.project.dto.RegisterDTO;
import com.coffee.project.mapper.UserMapper;
import com.coffee.project.service.UserService;
import com.coffee.project.utils.FileUploadUtils;
import com.coffee.project.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * UserServiceImpl 是用户服务实现类
 *
 * 功能：
 * 1. 用户注册
 * 2. 用户登录
 * 3. 密码重置
 * 4. 修改昵称
 * 5. 上传头像
 *
 * 特点：
 * - 使用 BCrypt 加密密码，保证安全性
 * - 登录成功生成 JWT Token
 * - 返回统一 Result 对象封装操作结果
 * - 日志记录关键操作和异常，便于排查问题
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;  // 自动注入 UserMapper，用于操作数据库

    /**
     * 用户注册
     *
     * 流程：
     * 1. 检查用户名和邮箱是否已被注册
     * 2. 校验密码与确认密码是否一致
     * 3. 使用 BCrypt 加密密码
     * 4. 插入用户到数据库
     *
     * @param registerDTO 前端提交的注册信息（用户名、邮箱、密码、确认密码）
     * @return Result<Object> 返回操作结果（成功或失败）
     */
    @Override
    public Result<Object> register(RegisterDTO registerDTO) {
        log.info("注册请求 -> 用户名: {}, 邮箱: {}", registerDTO.getUsername(), registerDTO.getEmail());

        // 查询是否有相同用户名或邮箱的用户
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

        // 校验密码与确认密码是否一致
        if (!Objects.equals(registerDTO.getPassword(), registerDTO.getConfirmPassword())) {
            log.warn("注册失败：密码不一致 -> 用户名: {}", registerDTO.getUsername());
            return Result.error("输入密码和确认密码不同，请重新输入");
        }

        // 创建新用户对象
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(BCrypt.hashpw(registerDTO.getPassword(), BCrypt.gensalt())); // 加密密码
        user.setName(registerDTO.getUsername()); // 默认昵称和用户名一样
        user.setCreateTime(new Date()); // 设置创建时间

        // 插入数据库
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
     * 流程：
     * 1. 根据用户名查询用户
     * 2. 验证用户是否存在
     * 3. 验证密码是否正确（BCrypt 校验）
     * 4. 生成 JWT Token
     * 5. 返回用户信息和 Token
     *
     * @param loginDTO 前端传来的登录信息（用户名、密码）
     * @return Result<Object> 登录结果，包括用户信息和 JWT Token
     */
    @Override
    public Result<Object> login(LoginDTO loginDTO) {
        log.info("用户登录尝试，用户名: {}", loginDTO.getUsername());

        // 查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginDTO.getUsername());
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            log.info("登录失败，用户名不存在: {}", loginDTO.getUsername());
            return Result.error("用户名或密码错误");
        }

        // 验证密码
        if (!BCrypt.checkpw(loginDTO.getPassword(), user.getPassword())) {
            log.info("登录失败，密码错误，用户名: {}", loginDTO.getUsername());
            return Result.error("用户名或密码错误");
        }

        // 生成 JWT
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        String jwt = JwtUtils.generateJwt(claims);

        log.info("登录成功，用户名: {}, JWT 生成完成", loginDTO.getUsername());

        // 封装登录结果
        LoginResultDTO loginResultDTO = new LoginResultDTO();
        loginResultDTO.setToken(jwt);
        loginResultDTO.setUser(user);

        return Result.success(loginResultDTO);
    }

    /**
     * 密码重置
     *
     * 流程：
     * 1. 验证用户名和邮箱是否匹配
     * 2. 检查新密码是否为空或与旧密码相同
     * 3. 使用 BCrypt 加密新密码
     * 4. 更新数据库
     *
     * @param passwordResetDTO 前端传来的密码重置信息
     * @return Result<Object> 密码重置结果
     */
    @Override
    public Result<Object> passwordReset(PasswordResetDTO passwordResetDTO) {
        log.info("密码重置请求开始，用户名: {}", passwordResetDTO.getUsername());

        // 查询用户是否存在且邮箱匹配
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", passwordResetDTO.getUsername())
                .eq("email", passwordResetDTO.getEmail());
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            log.warn("密码重置失败：用户名或邮箱不匹配，用户名: {}", passwordResetDTO.getUsername());
            return Result.error("用户名或邮箱不匹配");
        }

        // 检查新密码是否为空
        if (passwordResetDTO.getNewPassword() == null || passwordResetDTO.getNewPassword().isEmpty()) {
            log.warn("密码重置失败：新密码为空，用户名: {}", passwordResetDTO.getUsername());
            return Result.error("新密码不能为空");
        }

        // 检查新密码是否与旧密码相同
        if (BCrypt.checkpw(passwordResetDTO.getNewPassword(), user.getPassword())) {
            log.warn("密码重置失败：新密码不能与旧密码相同，用户名: {}", passwordResetDTO.getUsername());
            return Result.error("新密码不能与旧密码相同");
        }

        // 加密新密码并更新
        user.setPassword(BCrypt.hashpw(passwordResetDTO.getNewPassword(), BCrypt.gensalt()));
        int rows = userMapper.updateById(user);
        if (rows <= 0) {
            log.error("密码重置失败：数据库更新失败，用户名: {}", passwordResetDTO.getUsername());
            return Result.error("密码重置失败，请重试");
        }

        log.info("密码重置成功，用户名: {}", passwordResetDTO.getUsername());
        return Result.success("密码重置成功");
    }

    /**
     * 修改用户昵称
     *
     * @param id 用户ID
     * @param name 新昵称
     * @return Result<Object> 修改结果
     */
    @Override
    public Result<Object> updateName(Long id, String name) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.error("用户不存在,更新名字失败！");
        }

        user.setName(name);
        int rows = userMapper.updateById(user);
        if (rows > 0) {
            return Result.success("修改名字成功");
        } else {
            return Result.error("修改名字失败");
        }
    }

    /**
     * 上传头像并更新用户 avatarUrl
     *
     * 流程：
     * 1. 根据用户 ID 查询用户
     * 2. 使用 FileUploadUtils 上传头像，返回 URL
     * 3. 更新用户 avatarUrl 并保存到数据库
     *
     * @param id 用户ID
     * @param file 上传的头像文件
     * @return Result<Object> 返回头像 URL
     */
    @Override
    public Result<Object> updateAvatar(Long id, MultipartFile file) throws IOException {
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.error("用户不存在，更新头像失败！");
        }

        // 上传文件并返回访问 URL
        String avatarUrl = FileUploadUtils.upload(file);

        // 更新数据库
        user.setAvatarUrl(avatarUrl);
        userMapper.updateById(user);

        return Result.success(avatarUrl); // 返回前端可以访问的 URL
    }
}
