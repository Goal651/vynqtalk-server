package com.vynqtalk.server.dto.messages;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageUpdateRequest {
    @NotBlank(message = "Content is required")
    private String content;
}

// Note: Use this DTO for message update requests only. 