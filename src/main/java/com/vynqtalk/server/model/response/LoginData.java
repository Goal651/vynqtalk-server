package com.vynqtalk.server.model.response;

import com.vynqtalk.server.model.User;

import lombok.*;

@Data
@AllArgsConstructor
public class LoginData {
    private User user;
    private String accessToken;
}