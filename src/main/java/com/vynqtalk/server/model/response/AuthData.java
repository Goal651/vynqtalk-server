package com.vynqtalk.server.model.response;

import com.vynqtalk.server.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;



@Data
@AllArgsConstructor
public class AuthData {
    private User user;
    private String accessToken;
}