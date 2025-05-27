package com.vynqtalk.server.controller;

import com.vynqtalk.server.model.ChatMessage;
import com.vynqtalk.server.model.Message;
import com.vynqtalk.server.service.MessageService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private MessageService messageService;

    @MessageMapping("/chat.sendMessage") // front-end will send to /app/chat.sendMessage
    @SendTo("/topic/public") // sent back to all clients subscribed to /topic/public
    public ChatMessage receiveMessage(@Payload ChatMessage message) {
        Message savedMessage = new Message();
        savedMessage.setSenderId(message.getSender());
        savedMessage.setReceiverId(message.getReceiver());
        savedMessage.setContent(message.getContent());
        savedMessage.setEdited(false);
        messageService.saveMessage(savedMessage);
        return message;
    }
}
