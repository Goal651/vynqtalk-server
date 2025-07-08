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

    // Success response constructor
    public ApiResponse(T data, String message,boolean success) {
        this.success = success;
        this.data = data;
        this.message = message;
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
}

// Note: Use this generic DTO for all API responses.