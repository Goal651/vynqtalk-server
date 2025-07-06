package com.vynqtalk.server.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    private String name;
    private String bio;
    private String status;
}
// Note: Use this DTO for user profile update requests only.