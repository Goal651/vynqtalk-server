package com.vynqtalk.server.dto;

import java.util.List;

public class GroupDTO {
    public Long id;
    public String name;
    public String description;
    public Boolean isPrivate;
    public List<UserDTO> members;
}
