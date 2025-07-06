package com.vynqtalk.server.dto.websocket;

import com.vynqtalk.server.model.messages.Message;
import com.vynqtalk.server.model.enums.MessageType;
import com.vynqtalk.server.model.users.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Payload for replying to a private chat message via WebSocket.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageReply  {
    @NotNull
    private User sender;
    @NotNull
    private User receiver;
    @NotBlank
    private String content;
    @NotBlank
    private MessageType type;
    @NotNull
    private Message replyTo;

    private String fileName;
}

