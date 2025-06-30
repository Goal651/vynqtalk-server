package com.vynqtalk.server.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.Instant;

import com.vynqtalk.server.model.enums.UserRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "isAdmin is required")
    private UserRole userRole;

    private String status;
    private Instant lastActive;
}
// Note: Use this DTO for user profile update requests only. 