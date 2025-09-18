package com.coffee.project.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.coffee.project.common.Result;
import com.coffee.project.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录检查拦截器
 * 作用：
 *   1. 在请求到达 Controller 之前，统一进行登录状态校验。
 *   2. 通过解析 JWT 判断用户是否已经登录。
 *   3. 未登录或 Token 无效时，拦截请求并返回统一的错误信息。
 */
@Slf4j
@Component // 交给 Spring 容器管理，自动注册为 Bean
public class LoginCheckInterceptor implements HandlerInterceptor {

    /**
     * preHandle 方法：
     *  - 在 Controller 方法执行前调用
     *  - 返回 true：放行请求
     *  - 返回 false：拦截请求
     */
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {

        // 1. 获取本次请求的 URL 路径
        String url = req.getRequestURI();
        log.info("请求的url {}", url);

        // 2. 从请求头中获取 JWT Token
        String jwt = req.getHeader("Authorization");

        /**
         * 3. 判断 Token 是否为空
         *    - 如果为空，说明前端没有携带 Token
         *    - 直接返回未登录的统一错误信息，并拦截请求
         */
        if (!StringUtils.hasLength(jwt)) {
            log.info("请求头 Authorization 为空，返回未登录错误信息");

            // 封装统一的错误返回对象
            Result error = Result.error("NOT_LOGIN");

            // 转为 JSON 返回给前端
            resp.getWriter().write(JSONObject.toJSONString(error));
            return false;
        }

        /**
         * 4. 解析并验证 JWT Token
         *    - 如果解析失败（token 过期、签名不一致、格式错误等），说明未登录或非法访问
         */
        try {
            JwtUtils.parseJWT(jwt); // 验证 token 是否合法
        } catch (Exception e) {
            e.printStackTrace();
            log.info("解析令牌失败，返回未登录错误信息");

            // 返回错误信息
            Result error = Result.error("NOT_LOGIN");
            resp.getWriter().write(JSONObject.toJSONString(error));
            return false;
        }

        // 6. Token 解析成功，说明用户已登录，放行请求
        log.info("令牌合法，放行请求");
        return true;
    }
}
