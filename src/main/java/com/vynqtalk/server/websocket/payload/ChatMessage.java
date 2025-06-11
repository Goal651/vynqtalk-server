package com.vynqtalk.server.websocket.payload;


import com.vynqtalk.server.model.User;

import lombok.Data;

@Data
public class ChatMessage  {
    private User sender;
    private User receiver;
    private String content;
    private String type; 
}

