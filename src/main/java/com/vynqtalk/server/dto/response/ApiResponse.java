package com.vynqtalk.server.dto.response;

import com.vynqtalk.server.interfaces.IResponse;
import lombok.Data;
import jakarta.annotation.Nullable;

@Data
public class ApiResponse<T> implements IResponse<T> {
    private final boolean success;
    private final T data;
    private final String message;
    private final int status;

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