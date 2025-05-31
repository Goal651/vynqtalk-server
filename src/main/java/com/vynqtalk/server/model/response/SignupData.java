package com.vynqtalk.server.model.response;

import com.vynqtalk.server.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupData {
    private String accessToken;
    private User user;

}