package com.vynqtalk.server.model.sockets;

import java.util.List;

import lombok.Data;

@Data
public class ReactMessage {
    private Long messageId;
    private List<String> reactions;
}
