package com.vynqtalk.server.dto.websocket;

import com.vynqtalk.server.model.enums.MessageType;
import com.vynqtalk.server.model.users.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Payload for sending a private chat message via WebSocket.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage  {
    @NotNull
    private User sender;
    @NotNull
    private User receiver;
    @NotBlank
    private String content;
    @NotBlank
    private MessageType type;
}

