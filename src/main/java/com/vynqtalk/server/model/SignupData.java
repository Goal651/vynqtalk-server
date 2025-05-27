package com.vynqtalk.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupData {
    private String access_token;
    private User user;

}