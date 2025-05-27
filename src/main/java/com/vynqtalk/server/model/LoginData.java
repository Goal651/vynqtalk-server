package com.vynqtalk.server.model;

import lombok.*;

@Data
@AllArgsConstructor
public class LoginData {
    private User user;
    private String accessToken;
}