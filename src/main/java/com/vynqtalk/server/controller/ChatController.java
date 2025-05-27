package com.vynqtalk.server.controller;

import com.vynqtalk.server.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/chat.sendMessage") // front-end will send to /app/chat.sendMessage
    @SendTo("/topic/public") // sent back to all clients subscribed to /topic/public
    public ChatMessage receiveMessage(@Payload ChatMessage chatMessage) {
        System.out.println("Received from front-end: " + chatMessage.getContent());
        return chatMessage;
    }
}
