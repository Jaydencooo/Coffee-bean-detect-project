package com.coffee.project.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Map;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * JwtUtils 工具类
 * 作用：用于生成和解析 JWT 令牌
 */
public class JwtUtils {

    /**
     * 生成一个安全的 HS256 算法密钥
     * 注意：
     *  - HS256 算法要求密钥长度 >= 32 字节，否则会抛 WeakKeyException 异常
     *  - 这里使用 JJWT 提供的 Keys.secretKeyFor() 方法自动生成符合要求的安全密钥
     *  - 如果每次重启项目都会重新生成密钥，则之前生成的 token 将会失效
     */
    private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * 生成 JWT 令牌
     * @param claims 需要写入到 token 中的业务数据，比如用户名、用户ID等
     * @return 生成的 JWT 字符串
     */
    public static String generateJwt(Map<String, Object> claims) {
        return Jwts.builder()
                // 设置 token 携带的自定义信息
                .setClaims(claims)
                // 使用密钥进行签名
                .signWith(KEY)
                // 生成最终的 token 字符串
                .compact();
    }

    /**
     * 解析 JWT 令牌
     * @param jwt 前端传入的 token 字符串
     * @return 解析出的 Claims 对象（包含 token 中携带的所有数据）
     * @throws io.jsonwebtoken.JwtException 如果 token 无效或过期，将抛出异常
     */
    public static Claims parseJWT(String jwt) {
        return Jwts.parserBuilder()
                // 设置解析 token 时需要使用的签名密钥
                .setSigningKey(KEY)
                // 构建解析器
                .build()
                // 解析并验证 token
                .parseClaimsJws(jwt)
                // 获取 token 中的 payload 部分（Claims）
                .getBody();
    }
}
