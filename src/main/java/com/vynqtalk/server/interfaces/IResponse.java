package com.vynqtalk.server.interfaces;

import jakarta.annotation.Nullable;

public interface IResponse<T> {
    boolean isSuccess();
    @Nullable T getData();
    @Nullable String getMessage();
}