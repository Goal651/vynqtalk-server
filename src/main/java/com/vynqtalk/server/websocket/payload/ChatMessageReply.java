package com.vynqtalk.server.websocket.payload;


import com.vynqtalk.server.model.Message;
import com.vynqtalk.server.model.User;

import lombok.Data;

@Data
public class ChatMessageReply  {
    private User sender;
    private User receiver;
    private String content;
    private String type; 
    private Message replyToMessage;
}

