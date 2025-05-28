package com.vynqtalk.server.model.response;

import java.util.List;

import com.vynqtalk.server.model.User;

import lombok.Data;

@Data
public class UsersResponse {

    private boolean success;
    private String message;
    private int status;
    private User user;
    private List<User> data;

    // Constructor for success response with users
    public UsersResponse(List<User> users, String message, int status) {
        this.success = true;
        this.data = users;
        this.message = message;
        this.status = status;
    }

    // Constructor for error response
    public UsersResponse(String message, int status) {
        this.success = false;
        this.message = message;
        this.status = status;
    }

}
