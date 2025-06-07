package com.vynqtalk.server.dto;

import java.time.Instant;
import java.util.List;

public class GroupDTO {
    public Long id;
    public String name;
    public String description;
    public Boolean isPrivate;
    public UserDTO createdBy;
    public Instant createdAt;
    public String status;
    public List<UserDTO> members;
}
