package com.coffee.project.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/coze")
public class CozeController {

    @Value("${coze.bot-id}")
    private String botId;

    @Value("${coze.pat}")
    private String pat;

    @GetMapping("/config")
    public Map<String, String> getConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("botId", botId);
        config.put("token", pat); // 生产环境这里需要改成生成临时 token
        return config;
    }
}
