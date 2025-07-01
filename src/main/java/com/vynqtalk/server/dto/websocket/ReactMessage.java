package com.vynqtalk.server.dto.websocket;

import java.util.List;

import com.vynqtalk.server.model.messages.Reaction;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Payload for reacting to a message via WebSocket.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReactMessage {
    @NotNull
    private Long messageId;
    @NotNull
    private List<Reaction> reactions;
}
