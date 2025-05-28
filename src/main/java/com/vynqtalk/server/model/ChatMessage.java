package com.vynqtalk.server.model;

import java.util.List;

import lombok.Data;

@Data
public class ChatMessage  {
    private String sender;
    private String receiver;
    private String content;
    private String type; 
    private List<String> reactions;
}

