package com.vynqtalk.server.dto.websocket;

import com.vynqtalk.server.model.Group;
import com.vynqtalk.server.model.GroupMessage;
import com.vynqtalk.server.model.User;
import com.vynqtalk.server.model.enums.MessageType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Payload for replying to a group chat message via WebSocket.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatGroupMessageReply  {
    @NotNull
    private User sender;
    @NotNull
    private Group group;
    @NotBlank
    private String content;
    @NotBlank
    private MessageType type;
    @NotNull
    private GroupMessage replyTo;
}

