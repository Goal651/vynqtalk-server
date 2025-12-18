package com.vynqtalk.server.dto.response;

import com.vynqtalk.server.dto.user.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthData {
    private UserDTO user;
    private String accessToken;
}