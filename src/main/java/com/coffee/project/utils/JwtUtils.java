package com.coffee.project.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * JwtUtils 工具类
 * 作用：
 *  1. 生成 JWT 令牌
 *  2. 解析 JWT 令牌
 *  3. 从 token 中获取用户 ID
 */
public class JwtUtils {

    // 固定秘钥（长度 >= 32 字节，保证 token 重启后仍然有效）
    private static final String SECRET = "ThisIsA32ByteSecretKeyForJWTExample!123";

    // 使用 SECRET 生成签名 Key
    private static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    // token 默认过期时间，单位毫秒（1小时）
    private static final long EXPIRATION = 3600_000;

    /**
     * 生成 JWT 令牌
     * 说明：
     *  1. 可以把需要的数据放在 claims 中（如 userId、username 等）
     *  2. 同时把 userId 写入 subject，保持兼容老方法
     *
     * @param claims 需要写入 token 的数据
     * @return JWT 字符串
     */
    public static String generateJwt(Map<String, Object> claims) {
        Date now = new Date();  // 当前时间
        Date expiryDate = new Date(now.getTime() + EXPIRATION); // 过期时间

        return Jwts.builder()
                .setClaims(claims) // 设置自定义 claims
                .setSubject(claims.get("userId") != null
                        ? String.valueOf(claims.get("userId"))
                        : null)  // 设置 subject（兼容旧方法）
                .setIssuedAt(now)   // 签发时间
                .setExpiration(expiryDate)  // 过期时间
                .signWith(KEY, SignatureAlgorithm.HS256) // 使用 HS256 算法签名
                .compact();  // 构建 token
    }

    /**
     * 解析 JWT 令牌
     * 说明：
     *  1. 校验 token 是否正确（签名、格式、过期）
     *  2. 返回解析后的 Claims 对象
     *
     * @param jwt JWT 字符串
     * @return Claims 对象
     */
    public static Claims parseJWT(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY) // 设置签名 Key
                .build()
                .parseClaimsJws(jwt) // 解析 token
                .getBody();          // 返回 payload（Claims）
    }

    /**
     * 从 JWT token 中获取用户 ID
     * 说明：
     *  1. 兼容老方法，生成 token 时 ID 放在 subject
     *  2. 如果 subject 为空，则尝试从 claims 里获取 "id" 字段
     *
     * @param token JWT 字符串
     * @return 用户 ID，如果解析失败返回 null
     */
    public static Long getUserId(String token) {
        Claims claims = parseJWT(token);   // 解析 token
        String subject = claims.getSubject();  // 获取 subject
        if (subject != null && !subject.isEmpty()) {
            return Long.valueOf(subject);
        }
        // 如果 subject 没有，尝试从 claims 里获取 id
        return claims.get("id", Long.class);
    }

}
