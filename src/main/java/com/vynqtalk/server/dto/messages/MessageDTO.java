package com.vynqtalk.server.dto.messages;

import java.time.Instant;
import java.util.List;

import com.vynqtalk.server.dto.user.UserDTO;
import com.vynqtalk.server.model.enums.MessageType;
import com.vynqtalk.server.model.messages.Reaction;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private Long id;
    private String content;
    private MessageType type;
    private UserDTO sender;
    private UserDTO receiver;
    private Instant timestamp;
    private String fileName;
    private boolean edited;
    private List<Reaction> reactions;
    private MessageDTO replyTo;
}

// Note: Use this DTO for message responses only (never for requests).