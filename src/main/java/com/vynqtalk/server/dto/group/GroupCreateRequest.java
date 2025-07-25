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
    private String description;
    private Boolean isPrivate;
}
