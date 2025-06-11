package com.vynqtalk.server.dto.messages;

import java.time.Instant;
import java.util.List;

import com.vynqtalk.server.dto.user.UserDTO;

public class MessageDTO {
    public Long id;
    public String content;
    public String type;
    public UserDTO sender;
    public UserDTO receiver;
    public Instant timestamp;
    public boolean edited;
    public List<String> reactions;
    public MessageDTO replyToMessage;
}