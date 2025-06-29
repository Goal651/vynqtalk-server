package com.vynqtalk.server.dto.group;

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

    @NotBlank(message = "Description is required")
    private String description;

    private Boolean isPrivate;
}

// Note: Use this DTO for group creation requests only. 