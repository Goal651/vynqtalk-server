package com.vynqtalk.server.error;

public class GroupMessageNotFoundException extends RuntimeException {
    public GroupMessageNotFoundException(String message) {
        super(message);
    }
} 