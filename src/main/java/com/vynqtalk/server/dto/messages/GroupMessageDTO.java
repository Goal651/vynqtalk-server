package com.vynqtalk.server.dto.messages;

import java.time.Instant;
import java.util.List;

import com.vynqtalk.server.dto.group.GroupDTO;
import com.vynqtalk.server.dto.user.UserDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMessageDTO {
    private Long id;
    private String content;
    private String type;
    private UserDTO sender;
    private GroupDTO group;
    private Instant timestamp;
    private List<String> reactions;
}

// Note: Use this DTO for group message responses only (never for requests).
