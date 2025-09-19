package com.coffee.project.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CodeUtil {

    /**
     * 生成数字验证码
     *
     * @param length 验证码长度
     * @return 6位随机数字验证码
     */
    public String generateCode(int length) {
        String chars = "0123456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        return code.toString();
    }
}

