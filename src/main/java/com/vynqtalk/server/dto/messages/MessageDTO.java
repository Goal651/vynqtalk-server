package com.vynqtalk.server.dto.messages;

import java.time.Instant;
import java.util.List;

import com.vynqtalk.server.dto.user.UserDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private Long id;
    private String content;
    private String type;
    private UserDTO sender;
    private UserDTO receiver;
    private Instant timestamp;
    private boolean edited;
    private List<String> reactions;
    private MessageDTO replyToMessage;
}

// Note: Use this DTO for message responses only (never for requests).