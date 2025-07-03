package com.vynqtalk.server.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupCreateRequest {
    @NotBlank(message = "Group name is required")
    private String name;

    private String description;

    private Boolean isPrivate;
}

// Note: Use this DTO for group creation requests only. 