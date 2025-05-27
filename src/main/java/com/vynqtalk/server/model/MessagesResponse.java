package com.vynqtalk.server.model;

import java.util.List;

import lombok.Data;

@Data
public class MessagesResponse {
    private boolean success;
    private String message;
    private int status;
    private List<Message> data;
    
    // Constructor for success response with messages
    public MessagesResponse(List<Message> messages, String message, int status) {
        this.success = true;
        this.data = messages;
        this.message = message;
        this.status = status;
    }

    // Constructor for error response
    public MessagesResponse(String message, int status) {
        this.success = false;
        this.message = message;
        this.status = status;
    }
}
