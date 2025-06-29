package com.vynqtalk.server.dto.group;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberRequest {
    @NotNull(message = "User ID is required")
    private Long userId;
}

// Note: Use this DTO for group member add/update requests only. 