package com.coffee.project.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * CozeController 是用于返回 Coze 相关配置的控制器
 *
 * 主要功能：
 * 1. 读取配置文件中的 botId 和 pat
 * 2. 提供一个接口让前端获取这些配置
 *
 * 技术点：
 * @RestController: 表示这是一个 REST 风格控制器，返回 JSON 数据
 * @RequestMapping("/coze"): 所有接口的前缀都是 /coze
 */
@RestController
@RequestMapping("/coze")
public class CozeController {

    /**
     * @Value("${coze.bot-id}")
     * 读取配置文件 application.properties 或 application.yml 中的 coze.bot-id 属性
     * 并赋值给 botId 变量
     */
    @Value("${coze.bot-id}")
    private String botId;

    /**
     * @Value("${coze.pat}")
     * 读取配置文件中的 coze.pat 属性（一般是 token 或私密信息）
     * 注意：生产环境中不建议直接返回 PAT，应该使用临时 token 或服务端代理
     */
    @Value("${coze.pat}")
    private String pat;

    /**
     * GET /coze/config 接口
     *
     * 作用：
     * 1. 将 botId 和 token 封装成 Map
     * 2. 返回给前端
     *
     * 返回格式：
     * {
     *     "botId": "xxxx",
     *     "token": "xxxx"
     * }
     *
     * 注意：
     * - 这里 token 直接返回只是示例，生产环境应生成临时 token 或做权限控制
     */
    @GetMapping("/config")
    public Map<String, String> getConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("botId", botId);   // 放入 botId
        config.put("token", pat);     // 放入 token（这里示例，生产环境要改成临时 token）
        return config;                // 返回给前端，Spring Boot 会自动转成 JSON
    }
}
