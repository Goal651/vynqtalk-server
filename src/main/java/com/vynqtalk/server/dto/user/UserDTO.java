package com.vynqtalk.server.dto.user;

import java.time.Instant;

import com.vynqtalk.server.model.enums.UserRole;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String avatar;
    private String email;
    private UserRole userRole;
    private String status;
    private String bio;
    private Instant lastActive;
    private Instant createdAt;
}

// Note: Use this DTO for user profile responses only (never include sensitive
// fields).