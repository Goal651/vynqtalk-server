package com.vynqtalk.server.error;

public class UserSettingsNotFoundException extends RuntimeException {
    public UserSettingsNotFoundException(String message) {
        super(message);
    }
} 