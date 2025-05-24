package com.vynqtalk.server.model;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;
    private int status;

    // Success response constructor
    public ApiResponse(T data, String message, int status) {
        this.success = true;
        this.data = data;
        this.message = message;
        this.status = status;
    }

    // Error response constructor
    public ApiResponse(String message, int status) {
        this.success = false;
        this.data = null;
        this.message = message;
        this.status = status;
    }
}