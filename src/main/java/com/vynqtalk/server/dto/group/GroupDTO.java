package com.vynqtalk.server.dto.group;

import java.time.Instant;
import java.util.List;

import com.vynqtalk.server.dto.user.UserDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean isPrivate;
    private UserDTO createdBy;
    private Instant createdAt;
    private String status;
    private List<UserDTO> members;
    private List<UserDTO> admins;
}

// Note: Use this DTO for group responses only (never for requests).
