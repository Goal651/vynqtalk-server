package com.vynqtalk.server.model;

import lombok.Data;

@Data
public class AuthResult {
    private boolean auth;
    private User user;

    public AuthResult(boolean auth, User user) {
        this.auth = auth;
        this.user = user;
    }

    public AuthResult() {
        this.auth = false;
        this.user = null;
    }

}
