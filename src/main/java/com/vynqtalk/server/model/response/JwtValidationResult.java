package com.vynqtalk.server.model.response;


public class JwtValidationResult {
    private final boolean valid;
    private final String username;
    private final String errorMessage;
    
    public JwtValidationResult(boolean valid, String username, String errorMessage) {
        this.valid = valid;
        this.username = username;
        this.errorMessage = errorMessage;
    }

    public boolean isValid() {
        return valid;
    }

    public String getUsername() {
        return username;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
