package com.vynqtalk.server.dto.response;

import com.vynqtalk.server.model.users.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthData {
    private User user;
    private String accessToken;
}