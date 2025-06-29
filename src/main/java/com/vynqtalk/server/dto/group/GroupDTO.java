package com.vynqtalk.server.dto.group;

import java.time.Instant;
import java.util.List;

import com.vynqtalk.server.dto.user.UserDTO;

public class GroupDTO {
    public Long id;
    public String name;
    public String description;
    public Boolean isPrivate;
    public UserDTO createdBy;
    public Instant createdAt;
    public String status;
    public List<UserDTO> members;
    public List<UserDTO> admins;
}
