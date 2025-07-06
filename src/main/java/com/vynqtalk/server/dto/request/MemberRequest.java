package com.vynqtalk.server.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequest {
    @NotNull(message = "User ID is required")
    private Long userId;

    private String action;
}

// Note: Use this DTO for group member add/update requests only. 