package com.vynqtalk.server.dto;

import java.time.Instant;
import java.util.List;

public class GroupMessageDTO {
    public Long id;
    public String content;
    public String type;
    public UserDTO sender;
    public GroupDTO group;
    public Instant timestamp;
    public List<String> reactions;
}
