package com.coffee.project.dto;

import com.coffee.project.domain.User;
import lombok.Data;

@Data
public class LoginResultDTO {
    private String token;
    private User user;
}
