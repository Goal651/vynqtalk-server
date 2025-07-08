package com.vynqtalk.server.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupUpdateRequest {
    @NotBlank(message = "Group name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;
    private Boolean isPrivate;
}