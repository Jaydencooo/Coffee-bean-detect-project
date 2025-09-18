package com.coffee.project.dto;

import com.coffee.project.domain.User;
import lombok.Data;

@Data
public class LoginResult {
    private String token;
    private User user;
}
