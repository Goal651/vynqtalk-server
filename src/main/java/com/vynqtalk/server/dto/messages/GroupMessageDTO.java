package com.vynqtalk.server.dto.messages;

import java.time.Instant;
import java.util.List;

import com.vynqtalk.server.dto.group.GroupDTO;
import com.vynqtalk.server.dto.user.UserDTO;

public class GroupMessageDTO {
    public Long id;
    public String content;
    public String type;
    public UserDTO sender;
    public GroupDTO group;
    public Instant timestamp;
    public List<String> reactions;
}
