package com.vynqtalk.server.model;


import lombok.Data;

@Data
public class ChatMessageReply  {
    private Long senderId;
    private Long receiverId;
    private String content;
    private String type; 
    private Message replyToMessage;
}

