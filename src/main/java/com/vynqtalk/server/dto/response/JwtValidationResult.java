package com.vynqtalk.server.dto.response;

import lombok.Data;

@Data
public class JwtValidationResult {
    private final boolean valid;
    private final String username;
    private final String errorMessage;
    
    public JwtValidationResult(boolean valid, String username, String errorMessage) {
        this.valid = valid;
        this.username = username;
        this.errorMessage = errorMessage;
    }
}
