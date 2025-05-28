package com.vynqtalk.server.model.response;

import com.vynqtalk.server.model.SignupData;

import lombok.Data;

@Data
public class SignupResponse<T extends SignupData> {
    private boolean success;
    private T data;
    private String message;

    // Success response constructor
    public SignupResponse(T data, String message) {
        this.success = true;
        this.data = data;
        this.message = message;

    }
    // Error response constructor
    public SignupResponse(String message) {
        this.success = false;
        this.data = null;
        this.message = message;
    }

}