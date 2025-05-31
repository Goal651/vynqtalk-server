package com.vynqtalk.server.model.sockets;


import lombok.Data;

@Data
public class ChatMessage  {
    private Long senderId;
    private Long receiverId;
    private String content;
    private String type; 
}

