package com.vynqtalk.server.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtValidation {
    private boolean valid;
    private String username;
    private String errorMessage;
}

