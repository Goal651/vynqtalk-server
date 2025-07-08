package com.vynqtalk.server.dto.websocket;

import com.vynqtalk.server.model.enums.MessageType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Payload for sending a group chat message via WebSocket.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatGroupMessage {
    @NotNull
    private Long senderId;
    @NotNull
    private Long groupId;
    @NotBlank
    private String content;
    @NotBlank
    private MessageType type;

    private String fileName;
}
