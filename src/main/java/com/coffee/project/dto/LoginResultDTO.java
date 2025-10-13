package com.coffee.project.dto;

import com.coffee.project.domain.User;
import lombok.Data;

/**
 * 登录结果数据传输对象（DTO）。
 * <p>
 * 该类用于封装用户登录成功后的返回信息，主要包括生成的令牌（Token）和用户的基本信息。
 * 它主要用于在登录接口中向客户端返回登录结果，便于客户端进行后续的认证和授权操作。
 * </p>
 */
@Data
public class LoginResultDTO {
    /**
     * 用户登录成功后生成的令牌（Token）。
     * <p>
     * 该令牌用于后续的请求认证，客户端需要在每次请求时携带该令牌，以便服务器验证用户身份。
     * 令牌通常具有一定的有效期限，过期后需要重新登录获取新的令牌。
     * </p>
     */
    private String token;

    /**
     * 登录用户的详细信息。
     * <p>
     * 该字段存储了登录用户的详细信息，包括用户名、电子邮件、电话号码等。
     * 这些信息可用于在客户端显示用户的基本信息，或者进行其他业务逻辑处理。
     * </p>
     */
    private User user;
}