package com.vynqtalk.server.dto.response;

import com.vynqtalk.server.interfaces.IResponse;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.annotation.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> implements IResponse<T> {
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

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    @Nullable
    public T getData() {
        return data;
    }

    @Override
    @Nullable
    public String getMessage() {
        return message;
    }

    @Override
    public int getStatus() {
        return status;
    }
}

// Note: Use this generic DTO for all API responses.